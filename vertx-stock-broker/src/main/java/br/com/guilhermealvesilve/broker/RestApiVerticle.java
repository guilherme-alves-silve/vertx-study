package br.com.guilhermealvesilve.broker;

import br.com.guilhermealvesilve.broker.assets.AssetsRestApi;
import br.com.guilhermealvesilve.broker.config.BrokerConfig;
import br.com.guilhermealvesilve.broker.config.ConfigLoader;
import br.com.guilhermealvesilve.broker.quotes.QuotesRestApi;
import br.com.guilhermealvesilve.broker.watchlist.WatchListRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestApiVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(config -> {
        LOG.info("Retrieved configuration: {}", config);
        startHttpServerAndAttachRoutes(startPromise, config);
      });
  }

  private void startHttpServerAndAttachRoutes(Promise<Void> startPromise, BrokerConfig config) {
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
      .listen(config.serverPort(), http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("HTTP server started on port {}", config.serverPort());
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
