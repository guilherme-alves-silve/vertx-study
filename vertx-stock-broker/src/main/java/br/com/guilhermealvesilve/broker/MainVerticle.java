package br.com.guilhermealvesilve.broker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  public static final int PORT = 8888;

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> LOG.error("Unhandled:", error));
    vertx.deployVerticle(new MainVerticle())
      .onFailure(error -> LOG.error("Failed to deploy:", error))
      .onSuccess(id -> LOG.info("Deployed {} with id {}", MainVerticle.class.getSimpleName(), id));
  }

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.deployVerticle(RestApiVerticle.class.getName(),
        new DeploymentOptions()
          .setInstances(numberOfCores()))
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        LOG.info("Deployed {} with id {}", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });
  }

  private int numberOfCores() {
    return Math.max(1, Runtime.getRuntime().availableProcessors());
  }
}
