package br.com.guilhermealvesilve.broker.watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;

public class DeleteWatchListFromDatabaseHandler extends AbstractWatchListFromDatabaseHandler {

  public DeleteWatchListFromDatabaseHandler(PgPool pool) {
    super(pool);
  }

  @Override
  public void handle(RoutingContext context) {

  }
}
