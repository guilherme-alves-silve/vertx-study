package br.com.guilhermealvesilve.broker.quotes;

import br.com.guilhermealvesilve.broker.assets.Asset;
import br.com.guilhermealvesilve.broker.assets.AssetsRestApi;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class QuotesRestApi {

  public static void attach(Router parent) {
    final var cachedQuotes = new HashMap<String, Quote>();
    AssetsRestApi.ASSETS.forEach(asset -> cachedQuotes.put(asset, initRandomQuote(asset)));
    parent.get("/quotes/:asset").handler(new GetQuoteHandler(Collections.unmodifiableMap(cachedQuotes)));
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
