package br.com.guilhermealvesilve.broker.assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
      .onFailure(error -> {
        LOG.error("Failure: ", error);
        context.response()
          .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
          .end(new JsonObject()
            .put("message", "Failed to get assets from database!")
            .put("path", context.normalizedPath())
            .toBuffer());
      })
      .onSuccess(result -> {
        var response = new JsonArray();
        result.forEach(row -> {
          response.add(row.getValue("name"));
        });

        LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
        context.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(response.toBuffer());
      });
  }
}
