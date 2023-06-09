package br.com.guilhermealvesilve.broker.assets;

import br.com.guilhermealvesilve.broker.web.HttpResponses;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetAssetsHandlerFromDatabaseHandler implements Handler<RoutingContext> {

  private final Pool pool;

  public GetAssetsHandlerFromDatabaseHandler(Pool pool) {
    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext context) {

    pool.query("SELECT name FROM broker.assets")
      .execute()
      .onFailure(HttpResponses.internalServerErrorHandler("Failed to get assets from database!", context))
      .onSuccess(result -> {
        var response = new JsonArray();
        result.forEach(row -> {
          response.add(row.getValue("name"));
        });

        HttpResponses.ok(context, response);
      });
  }
}
