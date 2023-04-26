package br.com.guilhermealvesilve.broker.watchlist;

import br.com.guilhermealvesilve.broker.web.HttpResponses;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.SqlResult;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.List;
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

    pool.withTransaction(client ->
      SqlTemplate.forUpdate(client,
        "DELETE FROM broker.watchlist WHERE account_id = #{account_id}")
        .execute(Map.of("account_id", accountId))
        .onFailure(HttpResponses.internalServerErrorHandler("Failed to clear watchlist for accountId: " + accountId, context))
        .compose(deletionDone -> addAllForAccountId(parameterBatch, client))
        .onFailure(HttpResponses.internalServerErrorHandler("Failed to add watchlist for accountId: " + accountId, context))
        .onSuccess(none -> HttpResponses.noContent(context)));
  }

  private Future<SqlResult<Void>> addAllForAccountId(final List<Map<String, Object>> parameterBatch,
                                                     final SqlConnection client) {
    return SqlTemplate.forUpdate(client,
      "INSERT INTO broker.watchlist(account_id, asset) VALUES (#{account_id}, #{asset}) " +
        "ON CONFLICT(account_id, asset) DO NOTHING")
      .executeBatch(parameterBatch);
  }
}
