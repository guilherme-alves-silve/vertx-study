package br.com.guilhermealvesilve.broker.watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;

public class PutWatchListFromDatabaseHandler extends AbstractWatchListFromDatabaseHandler {

  public PutWatchListFromDatabaseHandler(PgPool pool) {
    super(pool);
  }

  @Override
  public void handle(RoutingContext context) {

  }
}
