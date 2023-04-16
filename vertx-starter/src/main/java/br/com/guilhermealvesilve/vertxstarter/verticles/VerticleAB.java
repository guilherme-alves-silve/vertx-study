package br.com.guilhermealvesilve.vertxstarter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleAB extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticleAB.class);

  @Override
  public void start(Promise<Void> startPromise) {
    LOG.debug("Start {}", getClass().getSimpleName());
    startPromise.complete();
  }

  @Override
  public void stop(Promise<Void> stopPromise) {
    LOG.debug("Stop {}", getClass().getSimpleName());
    stopPromise.complete();
  }
}
