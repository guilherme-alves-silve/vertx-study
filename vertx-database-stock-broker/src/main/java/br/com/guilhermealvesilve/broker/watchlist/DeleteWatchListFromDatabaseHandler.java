package br.com.guilhermealvesilve.broker.watchlist;

import br.com.guilhermealvesilve.broker.web.HttpResponses;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.Map;

public class DeleteWatchListFromDatabaseHandler extends AbstractWatchListFromDatabaseHandler {

  public DeleteWatchListFromDatabaseHandler(Pool pool) {
    super(pool);
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = getAccountId(context);
    SqlTemplate.forUpdate(pool,
      "DELETE FROM broker.watchlist WHERE account_id = #{account_id}")
      .execute(Map.of("account_id", accountId))
      .onFailure(HttpResponses.internalServerErrorHandler("Failed to fetch watchlist for accountId: " + accountId, context))
      .onSuccess(result -> HttpResponses.noContent(context, result));
  }
}
