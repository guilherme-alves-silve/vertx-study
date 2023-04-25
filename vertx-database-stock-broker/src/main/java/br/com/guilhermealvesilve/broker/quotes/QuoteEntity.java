package br.com.guilhermealvesilve.broker.quotes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;

/**
 * Reference:
 *  https://dev.to/brunooliveira/practical-java-16-using-jackson-to-serialize-records-4og4
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record QuoteEntity(String asset,
                          BigDecimal bid,
                          BigDecimal ask,
                          @JsonProperty("last_price")
                          BigDecimal lastPrice,
                          BigDecimal volume) {

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
