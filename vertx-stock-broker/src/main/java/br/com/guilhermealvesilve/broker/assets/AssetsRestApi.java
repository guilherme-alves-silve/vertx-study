package br.com.guilhermealvesilve.broker.assets;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class AssetsRestApi {

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA");

  public static void attach(Router parent) {
    parent.get("/assets").handler(context -> {
      final var response = new JsonArray();
      ASSETS.stream().map(Asset::new).forEach(response::add);
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });
  }
}
