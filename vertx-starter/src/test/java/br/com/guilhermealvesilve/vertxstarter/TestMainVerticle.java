package br.com.guilhermealvesilve.vertxstarter;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  void deployVerticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticleOld(),
        testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void shouldCheckIfVerticleWasDeployed(Vertx vertx, VertxTestContext testContext) {
    testContext.completeNow();
  }
}
