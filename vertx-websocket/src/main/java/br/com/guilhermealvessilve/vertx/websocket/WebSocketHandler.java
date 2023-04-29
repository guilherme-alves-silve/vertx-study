package br.com.guilhermealvessilve.vertx.websocket;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketHandler implements Handler<ServerWebSocket> {

  public static final String PATH = "/ws/simple/prices";

  private final PriceBroadcast broadcast;

  public WebSocketHandler(final Vertx vertx) {
    this.broadcast = new PriceBroadcast(vertx);
  }

  @Override
  public void handle(ServerWebSocket ws) {
    if (!PATH.equalsIgnoreCase(ws.path())) {
      LOG.info("Reject wrong path: {}", ws.path());
      ws.writeFinalTextFrame("Wrong path. Only " + PATH + " is accepted!");
      closeClient(ws);
      return;
    }

    LOG.info("Opening web socket connection: {}, {}", ws.path(), ws.textHandlerID());
    ws.accept();
    ws.frameHandler(frameHandler(ws));
    ws.endHandler(onClose -> {
      LOG.info("Closed {}", ws.textHandlerID());
      broadcast.unregister(ws);
    });
    ws.exceptionHandler(err -> LOG.error("Failed: ", err));
    ws.writeTextMessage("Connected!");
    broadcast.register(ws);
  }

  private Handler<WebSocketFrame> frameHandler(ServerWebSocket ws) {
    return received -> {
      final var msg = received.textData();
      LOG.debug("Received message: {} from client {}", msg, ws.textHandlerID());
      if ("disconnect me".equalsIgnoreCase(msg)) {
        LOG.info("Client close requested!");
        closeClient(ws);
      } else {
        ws.writeTextMessage("Not supported => (" + msg + ")");
      }
    };
  }

  private void closeClient(ServerWebSocket ws) {
    ws.close((short) 1000, "Normal Closure");
  }
}
