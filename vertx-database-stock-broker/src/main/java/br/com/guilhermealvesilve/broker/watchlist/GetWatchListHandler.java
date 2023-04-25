package br.com.guilhermealvesilve.broker.watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GetWatchListHandler extends AbstractWatchListHandler {

  private final Map<UUID, WatchList> uuidAndWatchList;

  public GetWatchListHandler(Map<UUID, WatchList> uuidAndWatchList) {
    this.uuidAndWatchList = uuidAndWatchList;
  }

  @Override
  public void handle(RoutingContext context) {
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
  }
}
