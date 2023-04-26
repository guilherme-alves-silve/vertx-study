package br.com.guilhermealvesilve.broker.watchlist;

import br.com.guilhermealvesilve.broker.web.HttpResponses;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.Map;

public class PutWatchListFromDatabaseHandler extends AbstractWatchListFromDatabaseHandler {

  public PutWatchListFromDatabaseHandler(PgPool pool) {
    super(pool);
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = getAccountId(context);
    var watchList = context.body().asPojo(WatchList.class);

    final var parameterBatch = watchList.assets()
      .stream()
      .map(asset -> Map.<String, Object>of("account_id", accountId,
        "asset", asset.name()))
      .toList();

    SqlTemplate.forUpdate(pool,
        "INSERT INTO broker.watchlist(account_id, asset) VALUES (#{account_id}, #{asset}) " +
          "ON CONFLICT(account_id, asset) DO NOTHING")
      .executeBatch(parameterBatch)
      .onFailure(HttpResponses.internalServerErrorHandler("Failed to fetch watchlist for accountId: " + accountId, context))
      .onSuccess(none -> HttpResponses.notContent(context));
  }
}
