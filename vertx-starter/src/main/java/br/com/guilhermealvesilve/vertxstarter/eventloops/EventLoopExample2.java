package br.com.guilhermealvesilve.vertxstarter.eventloops;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopExample2 extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(EventLoopExample2.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx(new VertxOptions()
      .setMaxEventLoopExecuteTime(500)
      .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
      .setBlockedThreadCheckInterval(1L)
      .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS));
    vertx.deployVerticle(EventLoopExample2.class.getName(),
        new DeploymentOptions().setInstances(4));
  }

  @Override
  public void start(Promise<Void> startPromise) throws InterruptedException {
    LOG.debug("Start {}", getClass().getSimpleName());
    startPromise.complete();
    // do not do this inside a verticle
    Thread.sleep(5000);
  }
}
