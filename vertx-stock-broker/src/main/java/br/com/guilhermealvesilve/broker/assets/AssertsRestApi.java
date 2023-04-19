package br.com.guilhermealvesilve.broker.assets;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssertsRestApi {

  public static void attach(Router parent) {
    parent.get("/assets").handler(context -> {
      final var response = new JsonArray();
      response
        .add(new Asset("AAPL"))
        .add(new Asset("AMZN"))
        .add(new Asset("NFLX"))
        .add(new Asset("TSLA"));
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });
  }
}
