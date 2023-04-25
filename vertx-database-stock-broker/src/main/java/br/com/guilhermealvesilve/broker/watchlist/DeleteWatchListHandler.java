package br.com.guilhermealvesilve.broker.watchlist;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Slf4j
public class DeleteWatchListHandler extends AbstractWatchListHandler {

  private final Map<UUID, WatchList> uuidAndWatchList;

  public DeleteWatchListHandler(Map<UUID, WatchList> uuidAndWatchList) {
    this.uuidAndWatchList = uuidAndWatchList;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = getAccountId(context);
    var deleted = uuidAndWatchList.remove(UUID.fromString(accountId));
    LOG.info("Deleted: {}, Remaining: {}", deleted, uuidAndWatchList.values());
    context.response()
      .end(deleted.toJsonObject().toBuffer());
  }
}
