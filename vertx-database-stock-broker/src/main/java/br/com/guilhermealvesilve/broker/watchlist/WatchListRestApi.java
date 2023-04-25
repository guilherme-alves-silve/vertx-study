package br.com.guilhermealvesilve.broker.watchlist;

import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WatchListRestApi {

  public static void attach(final Router parent) {
    final var uuidAndWatchList = new ConcurrentHashMap<UUID, WatchList>();
    final var path = "/account/watchlist/:accountId";
    parent.get(path).handler(new GetWatchListHandler(uuidAndWatchList));
    parent.put(path).handler(new PutWatchListHandler(uuidAndWatchList));
    parent.delete(path).handler(new DeleteWatchListHandler(uuidAndWatchList));
  }
}
