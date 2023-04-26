package br.com.guilhermealvesilve.broker.watchlist;

import br.com.guilhermealvesilve.broker.web.HttpResponses;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;

import java.util.Collections;

public class GetWatchListFromDatabaseHandler extends AbstractWatchListFromDatabaseHandler {

  public GetWatchListFromDatabaseHandler(Pool pool) {
    super(pool);
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = getAccountId(context);
    SqlTemplate.forQuery(pool,
      "SELECT asset FROM broker.watchlist WHERE account_id=#{account_id}")
      .mapTo(Row::toJson)
      .execute(Collections.singletonMap("account_id", accountId))
      .onFailure(HttpResponses.internalServerErrorHandler("Failed to fetch watchlist for accountId: " + accountId, context))
      .onSuccess(assets -> {
        if (!assets.iterator().hasNext()) {
          HttpResponses.notFound(String.format("Watchlist for accoutId %s is not available!", accountId), context);
          return;
        }

        var response = new JsonArray();
        assets.forEach(response::add);
        HttpResponses.ok(context, response);
      });
  }
}
