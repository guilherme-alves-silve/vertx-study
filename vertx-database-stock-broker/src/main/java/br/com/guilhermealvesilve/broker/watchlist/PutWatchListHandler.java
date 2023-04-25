package br.com.guilhermealvesilve.broker.watchlist;

import io.vertx.ext.web.RoutingContext;

import java.util.Map;
import java.util.UUID;

public class PutWatchListHandler extends AbstractWatchListHandler {

  private final Map<UUID, WatchList> uuidAndWatchList;

  public PutWatchListHandler(Map<UUID, WatchList> uuidAndWatchList) {
    this.uuidAndWatchList = uuidAndWatchList;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = getAccountId(context);
    var json = context.body().asJsonObject();
    var watchList = json.mapTo(WatchList.class);
    uuidAndWatchList.put(UUID.fromString(accountId), watchList);
    context.response().end(json.toBuffer());
  }
}
