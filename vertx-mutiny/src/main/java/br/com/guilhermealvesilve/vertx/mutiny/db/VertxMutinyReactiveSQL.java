package br.com.guilhermealvesilve.vertx.mutiny.db;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.Router;
import io.vertx.mutiny.ext.web.RoutingContext;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
public class VertxMutinyReactiveSQL extends AbstractVerticle {

  public static void main(String[] args) {
    var port = EmbeddedPostgres.startPostgres();
    var options = new DeploymentOptions()
      .setConfig(new JsonObject().put("port", port));

    final var vertx = Vertx.vertx();
    vertx.deployVerticle(VertxMutinyReactiveSQL::new, options)
      .subscribe().with(id -> LOG.info("Started: {}", id));
  }

  @Override
  public Uni<Void> asyncStart() {
    vertx.periodicStream(5000L)
      .toMulti()
      .subscribe().with(item -> LOG.info("Time: {}", LocalTime.now().getMinute()));

    var db = createPgPool(config());

    var router = Router.router(vertx);
    router.route().failureHandler(this::failureHandler);
    router.get("/users").respond(context -> executeQuery(db));

    return vertx.createHttpServer()
      .requestHandler(router)
      .listen(8111)
      .replaceWithVoid();
  }

  private Uni<JsonArray> executeQuery(PgPool db) {
    LOG.info("Executing DB query to find all users...");
    return db.query("SELECT * FROM users")
      .execute()
      .onItem().transform(rows -> {
        var data = new JsonArray();
        for (var row : rows) {
          data.add(row.toJson());
        }
        LOG.info("Return data: {}", data);
        return data;
      })
      .onFailure().invoke(error -> LOG.error("Failure query: ", error))
      .onFailure().recoverWithItem(new JsonArray());
  }

  private PgPool createPgPool(JsonObject config) {
    var connectOptions = new PgConnectOptions()
      .setHost("localhost")
      .setPort(config.getInteger("port"))
      .setDatabase(EmbeddedPostgres.DATABASE_NAME)
      .setUser(EmbeddedPostgres.USERNAME)
      .setPassword(EmbeddedPostgres.PASSWORD);

    var poolOptions = new PoolOptions()
      .setMaxSize(5);

    return PgPool.pool(vertx, connectOptions, poolOptions);
  }

  private void failureHandler(RoutingContext context) {
    context.response()
      .setStatusCode(500)
      .endAndForget("Something went wrong :(");
  }
}
