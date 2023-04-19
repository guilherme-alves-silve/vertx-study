package br.com.guilhermealvesilve.quotes;

import br.com.guilhermealvesilve.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class QuotesRestApi {

  public static void attach(Router parent) {
    parent.get("/quotes/:asset").handler(context -> {
      final var assetParam = context.pathParam("asset");
      LOG.debug("Asset parameter: {}", assetParam);

      final var quote = initRandomQuote(assetParam);
      final var response = quote.toJsonObject();
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
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
