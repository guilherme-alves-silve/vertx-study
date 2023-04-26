package br.com.guilhermealvesilve.broker.watchlist;

import br.com.guilhermealvesilve.broker.assets.Asset;
import br.com.guilhermealvesilve.broker.testutils.AbstractRestApiTest;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(VertxExtension.class)
public class TestWatchListPgRestApi extends AbstractRestApiTest {

  @Test
  void shouldAddsReturnAndDeleteWatchListForAccount(Vertx vertx, VertxTestContext testContext) {
    var client = webClient(vertx);
    var accountId = UUID.randomUUID();
    final var path = "/pg/account/watchlist/";
    client.put(path + accountId)
      .sendJsonObject(body())
      .onComplete(testContext.succeeding(response -> {
        LOG.info("Response PUT");
        assertEquals(204, response.statusCode());
      }))
      .compose(next -> client.put(path + accountId)
        .sendJsonObject(body())
        .onComplete(testContext.succeeding(response -> {
          LOG.info("Response PUT (Again)");
          assertEquals(204, response.statusCode());
      })))
      .compose(next -> client.get(path + accountId)
        .send()
        .onComplete(testContext.succeeding(response -> {
          var json = response.bodyAsJsonArray();
          LOG.info("Response GET: {}", json);
          assertAll(
            () -> assertEquals("[{\"asset\":\"AMZN\"},{\"asset\":\"TSLA\"}]", json.encode()),
            () -> assertEquals(200, response.statusCode())
          );
      })))
      .compose(next -> client.delete(path + accountId)
          .send()
          .onComplete(testContext.succeeding(response -> {
            LOG.info("Response DELETE");
            assertEquals(204, response.statusCode());
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
