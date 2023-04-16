package br.com.guilhermealvesilve.vertxstarter.json;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponseJSONExample {

  private static final String ADDRESS = "my.request.address";

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      var msg = new JsonObject()
        .put("message", "Hello World!")
        .put("version", 1);
      LOG.debug("Sending: {}", msg);
      eventBus.<JsonArray>request(ADDRESS, msg, reply -> {
        LOG.debug("Response: {}", reply.result().body());
      });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<JsonObject>consumer(ADDRESS, msg -> {
        LOG.debug("Received Message: {}", msg.body());
        msg.reply(new JsonArray()
          .add("one")
          .add("two")
          .add("three"));
      });
    }
  }
}
