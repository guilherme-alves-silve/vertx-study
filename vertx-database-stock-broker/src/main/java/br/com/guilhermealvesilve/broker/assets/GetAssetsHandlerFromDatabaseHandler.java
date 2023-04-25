package br.com.guilhermealvesilve.broker.assets;

import br.com.guilhermealvesilve.broker.web.HttpResponses;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import io.vertx.pgclient.PgPool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetAssetsHandlerFromDatabaseHandler implements Handler<RoutingContext> {

  private final PgPool pool;

  public GetAssetsHandlerFromDatabaseHandler(PgPool pool) {
    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext context) {

    pool.query("SELECT name FROM broker.assets")
      .execute()
      .onFailure(HttpResponses.errorHandler(HttpResponseStatus.INTERNAL_SERVER_ERROR, "Failed to get assets from database!", context))
      .onSuccess(result -> {
        var response = new JsonArray();
        result.forEach(row -> {
          response.add(row.getValue("name"));
        });

        HttpResponses.okResponse(context, response);
      });
  }
}
