package br.com.guilhermealvesilve.broker.watchlist;

import br.com.guilhermealvesilve.broker.assets.Asset;
import io.vertx.core.json.JsonObject;

import java.util.List;

public record WatchList(List<Asset> assets) {

  JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
