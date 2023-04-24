package br.com.guilhermealvesilve.broker.config;

import io.vertx.core.json.JsonObject;

import static br.com.guilhermealvesilve.broker.config.ConfigLoader.SERVER_PORT;

public record BrokerConfig(int serverPort, String version) {

  public static BrokerConfig from(final JsonObject config) {
    final var serverPort = config.getInteger(SERVER_PORT);
    if (null == serverPort) throw new RuntimeException(SERVER_PORT + " not configured!");
    final var version = config.getString("version");
    if (null == version) throw new RuntimeException("version is not configured in config file!");
    return new BrokerConfig(serverPort, version);
  }
}
