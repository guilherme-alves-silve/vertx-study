package br.com.guilhermealvesilve.vertxstarter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample3 extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(WorkerExample3.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample3());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-verticle")
      );
    startPromise.complete();
    vertx.executeBlocking(event -> {
      LOG.debug("Executing blocking code");
      try {
        Thread.sleep(5000);
        event.complete();
      } catch (InterruptedException ex) {
        LOG.error("Interrupted: ", ex);
        event.fail(ex);
      }
    }, result -> {
      if (result.succeeded()) {
        LOG.debug("Blocking call done.");
      } else {
        LOG.debug("Blocking call failed due to: ", result.cause());
      }
    });
  }
}
