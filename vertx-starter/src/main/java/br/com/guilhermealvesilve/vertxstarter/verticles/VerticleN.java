package br.com.guilhermealvesilve.vertxstarter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerticleN extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(VerticleN.class);

  @Override
  public void start(Promise<Void> startPromise) {
    LOG.debug("Start {} on thread {} with config {}",
      getClass().getSimpleName(), Thread.currentThread().getName(), config().toString());
    startPromise.complete();
  }
}
