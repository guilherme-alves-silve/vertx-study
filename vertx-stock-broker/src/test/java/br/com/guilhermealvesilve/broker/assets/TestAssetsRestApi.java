package br.com.guilhermealvesilve.broker.assets;

import br.com.guilhermealvesilve.broker.MainVerticle;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(VertxExtension.class)
public class TestAssetsRestApi {

  @BeforeEach
  void shouldDeployVerticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void shouldReturnAllAssets(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx,
      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    client.get("/assets")
        .send()
        .onComplete(testContext.succeeding(response -> {
          var expectedJson = "[{\"name\":\"AAPL\"},{\"name\":\"AMZN\"},{\"name\":\"FB\"},{\"name\":\"GOOG\"},{\"name\":\"MSFT\"},{\"name\":\"NFLX\"},{\"name\":\"TSLA\"}]";
          var json = response.bodyAsJsonArray();
          LOG.info("Response: {}", json);
          assertAll(
            () -> assertEquals(expectedJson, json.encode()),
            () -> assertEquals(200, response.statusCode()),
            () -> assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
              response.getHeader(HttpHeaders.CONTENT_TYPE.toString())),
            () -> assertEquals("my-value", response.getHeader("my-header"))
          );
          testContext.completeNow();
        }));
  }
}
