package br.com.guilhermealvesilve.broker.quotes;

import br.com.guilhermealvesilve.broker.web.HttpResponses;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public class GetQuoteHandlerFromDatabase implements Handler<RoutingContext> {

  private final Pool pool;

  public GetQuoteHandlerFromDatabase(Pool pool) {
    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext context) {
    final var assetParam = context.pathParam("asset");
    LOG.debug("Asset parameter: {}", assetParam);

    SqlTemplate.forQuery(pool,
        "SELECT q.asset, q.bid, q.ask, q.last_price, q.volume FROM broker.quotes q WHERE asset=#{asset}")
      .mapTo(QuoteEntity.class)
      .execute(Collections.singletonMap("asset", assetParam))
      .onFailure(HttpResponses.errorHandler(HttpResponseStatus.INTERNAL_SERVER_ERROR,
        String.format("Failed to get quote from asset %s from database!", assetParam), context))
      .onSuccess(quotes -> {
        if (!quotes.iterator().hasNext()) {
          HttpResponses.notFound("quote for asset " + assetParam + " not available!", context);
          return;
        }

        var response = quotes.iterator().next().toJsonObject();
        HttpResponses.ok(context, response);
      });
  }
}
