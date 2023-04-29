package br.com.guilhermealvessilve.vertx.websocket;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  public static final int PORT = 8902;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer(new HttpServerOptions()
        .setRegisterWebSocketWriteHandlers(true))
      .webSocketHandler(new WebSocketHandler(vertx))
      .listen(PORT, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.info("HTTP server started on port {}", PORT);
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
