package br.com.guilhermealvesilve.broker.config;

import br.com.guilhermealvesilve.broker.db.DbConfig;
import br.com.guilhermealvesilve.broker.db.DbType;
import io.vertx.core.json.JsonObject;

import static br.com.guilhermealvesilve.broker.config.ConfigLoader.SERVER_PORT;

public record BrokerConfig(int serverPort, String version, DbConfig dbConfig) {

  public static BrokerConfig from(final JsonObject config) {
    final var serverPort = config.getInteger(SERVER_PORT);
    if (null == serverPort) throw new RuntimeException(SERVER_PORT + " not configured!");
    final var version = config.getString("version");
    if (null == version) throw new RuntimeException("version is not configured in config file!");
    return new BrokerConfig(serverPort, version, parseDbConfig(config));
  }

  private static DbConfig parseDbConfig(JsonObject config) {
    return DbConfig.builder()
      .host(config.getString(ConfigLoader.DB_HOST))
      .port(config.getInteger(ConfigLoader.DB_PORT))
      .database(config.getString(ConfigLoader.DB_DATABASE))
      .user(config.getString(ConfigLoader.DB_USER))
      .password(config.getString(ConfigLoader.DB_PASSWORD))
      .type(DbType.valueOf(config.getString(ConfigLoader.DB_TYPE).toUpperCase()))
      .build();
  }
}
