package br.com.guilhermealvesilve.broker.testutils;

import br.com.guilhermealvesilve.broker.MainVerticle;
import br.com.guilhermealvesilve.broker.config.ConfigLoader;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;

@Slf4j
public abstract class AbstractRestApiTest {

  protected static final int TEST_SERVER_PORT = 9000;

  @BeforeEach
  void shouldDeployVerticle(Vertx vertx, VertxTestContext testContext) {
    System.setProperty(ConfigLoader.SERVER_PORT, String.valueOf(TEST_SERVER_PORT));
    System.setProperty(ConfigLoader.DB_HOST, "localhost");
    System.setProperty(ConfigLoader.DB_PORT, "5432");
    System.setProperty(ConfigLoader.DB_DATABASE, "vertx-stock-broker");
    System.setProperty(ConfigLoader.DB_USER, "postgres");
    System.setProperty(ConfigLoader.DB_PASSWORD, "secret");
    System.setProperty(ConfigLoader.DB_TYPE, "postgresql");
    LOG.warn("***Tests are using local database!***");
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  protected WebClient webClient(Vertx vertx) {
    return WebClient.create(vertx,
      new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }
}
