package br.com.guilhermealvesilve.broker;

import br.com.guilhermealvesilve.broker.assets.AssetsRestApi;
import br.com.guilhermealvesilve.broker.watchlist.WatchListRestApi;
import br.com.guilhermealvesilve.broker.quotes.QuotesRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  public static final int PORT = 8888;

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> LOG.error("Unhandled:", error));
    vertx.deployVerticle(new MainVerticle(), ar -> {
      if (ar.failed()) {
        LOG.error("Failed to deploy:", ar.cause());
        return;
      }

      LOG.info("Deployed {}!", MainVerticle.class.getSimpleName());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) {

    final Router restApi = Router.router(vertx);
    restApi.route()
      .handler(BodyHandler.create())
      .failureHandler(handleFailure());
    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
    WatchListRestApi.attach(restApi);

    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> LOG.error("HTTP Server error: ", error))
      .listen(PORT, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private Handler<RoutingContext> handleFailure() {
    return errorContext -> {
      if (errorContext.response().ended()) {
        // Ignore completed response
        return;
      }

      LOG.error("Route error: ", errorContext.failure());
      errorContext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong :(").toBuffer());
    };
  }
}
