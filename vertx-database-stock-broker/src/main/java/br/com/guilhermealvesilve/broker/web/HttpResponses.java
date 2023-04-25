package br.com.guilhermealvesilve.broker.web;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class HttpResponses {

  public static Handler<Throwable> errorHandler(final HttpResponseStatus status,
                                                final String msg,
                                                final RoutingContext context) {
    return error -> {
      LOG.error("Failure: ", error);
      errorResponse(status, msg, context);
    };
  }

  private static void errorResponse(HttpResponseStatus status, String msg, RoutingContext context) {
    context.response()
      .setStatusCode(status.code())
      .end(new JsonObject()
        .put("message", msg)
        .put("path", context.normalizedPath())
        .toBuffer());
  }

  private static void okResponse(final RoutingContext context,
                                 final String encoded,
                                 final Buffer responseBuffer) {
    LOG.info("Path {} responds with {}", context.normalizedPath(), encoded);
    context.response()
      .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .end(responseBuffer);
  }

  public static void notFound(final String msg,
                              final RoutingContext context) {
    errorResponse(HttpResponseStatus.NOT_FOUND, msg, context);
  }

  public static void okResponse(final RoutingContext context,
                                final JsonObject response) {
    okResponse(context, response.encode(), response.toBuffer());
  }

  public static void okResponse(final RoutingContext context,
                                final JsonArray response) {
    okResponse(context, response.encode(), response.toBuffer());
  }
}
