package br.com.guilhermealvessilve.vertx.websocket;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  void shouldDeployVerticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldConnectToWebSocketServer(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = vertx.createHttpClient(new HttpClientOptions());
    client.webSocket(MainVerticle.PORT, "localhost", WebSocketHandler.PATH)
        .onFailure(testContext::failNow)
        .onComplete(testContext.succeeding(ws -> {
          ws.handler(data -> {
            var receivedData = data.toString();
            LOG.debug("Received message: {}", receivedData);
            assertEquals("Connected!", receivedData);
            client.close();
            testContext.completeNow();
          });
        }));
  }

  @Test
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  void shouldReceiveMultipleMessagesUsingWebSocket(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = vertx.createHttpClient(new HttpClientOptions());
    final var counter = new AtomicInteger(0);
    final var expectedNumberOfMessages = 5;
    client.webSocket(MainVerticle.PORT, "localhost", WebSocketHandler.PATH)
      .onFailure(testContext::failNow)
      .onComplete(testContext.succeeding(ws -> {
        ws.handler(data -> {
          var receivedData = data.toString();
          LOG.debug("Received message: {}", receivedData);
          var currentValue = counter.getAndIncrement();
          if (currentValue > expectedNumberOfMessages) {
            assertTrue(receivedData.matches("\\{\"symbol\":\"AMZN\",\"value\":\\d{1,3}}"));
            client.close();
            testContext.completeNow();
          } else {
            LOG.debug("Not enough messages yet... ({}/{})", currentValue, expectedNumberOfMessages);
          }
        });
      }));
  }
}
