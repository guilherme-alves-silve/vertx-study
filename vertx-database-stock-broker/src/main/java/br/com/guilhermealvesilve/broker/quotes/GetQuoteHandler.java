package br.com.guilhermealvesilve.broker.quotes;

import br.com.guilhermealvesilve.broker.web.HttpResponses;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class GetQuoteHandler implements Handler<RoutingContext> {

  private final Map<String, Quote> cachedQuotes;

  public GetQuoteHandler(Map<String, Quote> cachedQuotes) {
    this.cachedQuotes = cachedQuotes;
  }

  @Override
  public void handle(RoutingContext context) {
    final var assetParam = context.pathParam("asset");
    LOG.debug("Asset parameter: {}", assetParam);

    final var maybeQuote = Optional.ofNullable(cachedQuotes.get(assetParam));
    if (maybeQuote.isEmpty()) {
      HttpResponses.notFound("quote for asset " + assetParam + " not available!", context);
      return;
    }

    final var response = maybeQuote.get().toJsonObject();
    LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
    context.response().end(response.toBuffer());
  }
}
