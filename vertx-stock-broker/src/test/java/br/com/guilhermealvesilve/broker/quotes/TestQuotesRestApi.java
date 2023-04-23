package br.com.guilhermealvesilve.broker.quotes;

import br.com.guilhermealvesilve.broker.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(VertxExtension.class)
public class TestQuotesRestApi {

  @BeforeEach
  void shouldDeployVerticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void shouldReturnQuoteForAsset(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx,
      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    client.get("/quotes/AMZN")
        .send()
        .onComplete(testContext.succeeding(response -> {
          var json = response.bodyAsJsonObject();
          LOG.info("Response: {}", json);
          assertAll(
            () -> assertEquals("AMZN", json.getJsonObject("asset").getString("name")),
            () -> assertInstanceOf(Number.class, json.getNumber("bid")),
            () -> assertInstanceOf(Number.class, json.getNumber("ask")),
            () -> assertInstanceOf(Number.class, json.getNumber("lastPrice")),
            () -> assertEquals(200, response.statusCode())
          );
          testContext.completeNow();
        }));
  }


  @Test
  void shouldReturnNotFoundForUnkownAsset(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx,
      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    client.get("/quotes/UNKNOWN")
      .send()
      .onComplete(testContext.succeeding(response -> {
        var expectedJson = "{\"message\":\"quote for asset UNKNOWN not available!\",\"path\":\"/quotes/UNKNOWN\"}";
        var json = response.bodyAsJsonObject();
        LOG.info("Response: {}", json);
        assertAll(
          () -> assertEquals("quote for asset UNKNOWN not available!", json.getString("message")),
          () -> assertEquals("/quotes/UNKNOWN", json.getString("path")),
          () -> assertEquals(expectedJson, json.encode()),
          () -> assertEquals(404, response.statusCode())
        );
        testContext.completeNow();
      }));
  }
}
