package br.com.guilhermealvesilve.broker;

import br.com.guilhermealvesilve.broker.config.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VersionInfoVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(config -> {
        LOG.info("Retrieved configuration: {}", config.version());
        startPromise.complete();
      });
  }
}
