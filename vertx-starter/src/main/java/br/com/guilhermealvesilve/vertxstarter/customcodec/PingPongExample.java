package br.com.guilhermealvesilve.vertxstarter.customcodec;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {

  private static final String ADDRESS = PingVerticle.class.getName();
  private static final Logger LOG = LoggerFactory.getLogger(PingPongExample.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), logOnError());
    vertx.deployVerticle(new PongVerticle(), logOnError());
  }

  private static Handler<AsyncResult<String>> logOnError() {
    return ar -> {
      if (ar.failed()) LOG.error("Error: ", ar.cause());
    };
  }

  static class PingVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PingVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      var ping = new Ping("Hello", true);
      LOG.debug("Sending: {}", ping);
      eventBus.<Pong>request(ADDRESS, ping, reply -> {
        if (reply.failed()) {
          LOG.error("Failed: ", reply.cause());
        }
        LOG.debug("Response: {}", reply.result().body());
      });
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      eventBus.<Ping>consumer(ADDRESS, msg -> {
        LOG.debug("Received Message: {}", msg.body());
        msg.reply(new Pong(0));
      }).exceptionHandler(error -> {
        LOG.error("Error: ", error);
      });
      startPromise.complete();
    }
  }
}
