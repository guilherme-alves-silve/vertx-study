package br.com.guilhermealvesilve.broker.quotes;

import br.com.guilhermealvesilve.broker.assets.Asset;
import br.com.guilhermealvesilve.broker.assets.AssetsRestApi;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class QuotesRestApi {

  public static void attach(Router parent) {
    final var cachedQuotes = new HashMap<String, Quote>();
    AssetsRestApi.ASSETS.forEach(asset -> cachedQuotes.put(asset, initRandomQuote(asset)));

    parent.get("/quotes/:asset").handler(context -> {
      final var assetParam = context.pathParam("asset");
      LOG.debug("Asset parameter: {}", assetParam);

      final var maybeQuote = Optional.ofNullable(cachedQuotes.get(assetParam));
      if (maybeQuote.isEmpty()) {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject()
            .put("message", "quote for asset " + assetParam + " not available!")
            .put("path", context.normalizedPath())
            .toBuffer());
        return;
      }

      final var response = maybeQuote.get().toJsonObject();
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });

  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .bid(randomValue())
      .ask(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
