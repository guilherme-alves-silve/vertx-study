package br.com.guilhermealvesilve.broker.watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class AbstractWatchListHandler implements Handler<RoutingContext> {

  protected String getAccountId(RoutingContext context) {
    var accountId = context.request().getParam("accountId");
    LOG.debug("{} for account {}", context.normalizedPath(), accountId);
    return accountId;
  }
}
