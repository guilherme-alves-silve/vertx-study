package br.com.guilhermealvesilve.broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class WatchListRestApi {

  public static void attach(final Router parent) {
    final var uuidAndWatchList = new HashMap<UUID, WatchList>();
    final var path = "/account/watchlist/:accountId";
    parent.get(path).handler(context -> {
      var accountId = getAccountId(context);
      var maybeWatchList = Optional.ofNullable(uuidAndWatchList.get(UUID.fromString(accountId)));
      if (maybeWatchList.isEmpty()) {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject()
            .put("message", "watchList for account " + accountId + " not available!")
            .put("path", context.normalizedPath())
            .toBuffer());
        return;
      }

      context.response().end(maybeWatchList.get().toJsonObject().toBuffer());
    });
    parent.put(path).handler(context -> {
      var accountId = getAccountId(context);
      var json = context.body().asJsonObject();
      var watchList = json.mapTo(WatchList.class);
      uuidAndWatchList.put(UUID.fromString(accountId), watchList);
      context.response().end(json.toBuffer());
    });
    parent.delete(path).handler(context -> {

    });
  }

  private static String getAccountId(RoutingContext context) {
    var accountId = context.request().getParam("accountId");
    LOG.debug("{} for account {}", context.normalizedPath(), accountId);
    return accountId;
  }
}
