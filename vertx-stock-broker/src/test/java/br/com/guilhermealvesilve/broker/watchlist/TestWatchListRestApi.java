package br.com.guilhermealvesilve.broker.watchlist;

import br.com.guilhermealvesilve.broker.MainVerticle;
import br.com.guilhermealvesilve.broker.assets.Asset;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(VertxExtension.class)
public class TestWatchListRestApi {

  @BeforeEach
  void shouldDeployVerticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void shouldAddsAndReturnWatchListForAccount(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx,
      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    var accountId = UUID.randomUUID();
    final var path = "/account/watchlist/";
    client.put(path + accountId)
      .sendJsonObject(body())
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("Response PUT: {}", json);
        assertAll(
          () -> assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode()),
          () -> assertEquals(200, response.statusCode())
        );
      }))
      .compose(next -> client.get(path + accountId)
        .send()
        .onComplete(testContext.succeeding(response -> {
          var json = response.bodyAsJsonObject();
          LOG.info("Response GET: {}", json);
          assertAll(
            () -> assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode()),
            () -> assertEquals(200, response.statusCode())
          );
          testContext.completeNow();
      })));
  }

  @Test
  void shouldAddsAndDeleteWatchListForAccount(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx,
      new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    var accountId = UUID.randomUUID();
    final var path = "/account/watchlist/";
    client.put(path + accountId)
      .sendJsonObject(body())
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("Response PUT: {}", json);
        assertAll(
          () -> assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode()),
          () -> assertEquals(200, response.statusCode())
        );
      }))
      .compose(next -> client.delete(path + accountId)
        .send()
        .onComplete(testContext.succeeding(response -> {
          var json = response.bodyAsJsonObject();
          LOG.info("Response DELETE: {}", json);
          assertAll(
            () -> assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode()),
            () -> assertEquals(200, response.statusCode())
          );
          testContext.completeNow();
        })));
  }

  private JsonObject body() {
    return new WatchList(List.of(
      new Asset("AMZN"),
      new Asset("TSLA")
    )).toJsonObject();
  }
}
