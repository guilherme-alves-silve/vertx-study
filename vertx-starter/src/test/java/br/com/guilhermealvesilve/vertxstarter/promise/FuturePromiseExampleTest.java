package br.com.guilhermealvesilve.vertxstarter.promise;

import br.com.guilhermealvesilve.vertxstarter.worker.WorkerExample2;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class FuturePromiseExampleTest {

  private static final Logger LOG = LoggerFactory.getLogger(WorkerExample2.class);

  @Test
  void shouldPromiseBeSuccessful(final Vertx vertx, final VertxTestContext context) {
    final var promise = Promise.<String>promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Completed");
      context.completeNow();
    });
    LOG.debug("End");
  }

  @Test
  void shouldPromiseFail(final Vertx vertx, final VertxTestContext context) {
    final var promise = Promise.<String>promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Completed");
      context.completeNow();
    });
    LOG.debug("End");
  }

  @Test
  void shouldFutureFail(final Vertx vertx, final VertxTestContext context) {
    final var promise = Promise.<String>promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Completed");
    });
    final Future<String> future = promise.future();
    future.onSuccess(context::failNow)
      .onFailure(result -> {
        LOG.debug("Result: {}", result);
        context.completeNow();
      });

    LOG.debug("End");
  }

  @Test
  void shouldFutureBeSuccessful(final Vertx vertx, final VertxTestContext context) {
    final var promise = Promise.<String>promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Timer done");
    });
    final Future<String> future = promise.future();
    future.onSuccess(result -> {
      LOG.debug("Result: {}", result);
      context.completeNow();
    }).onFailure(context::failNow);

    LOG.debug("End");
  }

  @Test
  void shouldMapFuture(final Vertx vertx, final VertxTestContext context) {
    final var promise = Promise.<String>promise();
    LOG.debug("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.debug("Timer done");
    });
    final Future<String> future = promise.future();
    future.map(value -> {
        LOG.debug("Map String to JsonObject");
        return new JsonObject()
          .put("key", value);
      })
      .map(json -> new JsonArray().add(json))
      .onSuccess(result -> {
      LOG.debug("Result: {} of type {}", result, result.getClass().getSimpleName());
      context.completeNow();
    }).onFailure(context::failNow);

    LOG.debug("End");
  }

  @Test
  void shouldFutureBeCoordinated(final Vertx vertx, final VertxTestContext context) {
    vertx.createHttpServer()
      .requestHandler(request -> LOG.debug("{}", request))
      .listen(10_000)
      .compose(server -> {
        LOG.info("Another task");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        LOG.info("Even more");
        return Future.succeededFuture(server);
      })
      .onFailure(context::failNow)
      .onSuccess(server -> {
        LOG.debug("Server started on port {}", server.actualPort());
        context.completeNow();
      });
  }

  @Test
  void shouldFutureBeComposedAndAllBeSuccessful(final Vertx vertx, final VertxTestContext context) {
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var three = Promise.<Void>promise();

    var futureOne = one.future();
    var futureTwo = two.future();
    var futureThree = three.future();

    CompositeFuture.all(futureOne, futureTwo, futureThree)
      .onFailure(context::failNow)
      .onSuccess(result -> {
        LOG.debug("Success");
        context.completeNow();
      });

    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.complete();
    });
  }

  @Test
  void shouldFutureBeComposedAndAnyBeSuccessful(final Vertx vertx, final VertxTestContext context) {
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var three = Promise.<Void>promise();

    var futureOne = one.future();
    var futureTwo = two.future();
    var futureThree = three.future();

    CompositeFuture.any(futureOne, futureTwo, futureThree)
      .onFailure(context::failNow)
      .onSuccess(result -> {
        LOG.debug("Success");
        context.completeNow();
      });

    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.fail("Three failed!");
    });
  }
}
