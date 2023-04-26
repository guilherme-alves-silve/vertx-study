package br.com.guilhermealvesilve.broker.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ConfigLoader {

  public static final String CONFIG_FILE = "application.yml";
  // Exposed environment variables
  public static final String SERVER_PORT = "SERVER_PORT";
  public static final String DB_HOST = "DB_HOST";
  public static final String DB_PORT = "DB_PORT";
  public static final String DB_DATABASE = "DB_DATABASE";
  public static final String DB_USER = "DB_USER";
  public static final String DB_PASSWORD = "DB_PASSWORD";
  public static final String DB_TYPE = "DB_TYPE";
  static final List<String> EXPOSED_ENVIRONMENT_VARIABLES = List.of(
    SERVER_PORT , DB_HOST, DB_PORT, DB_DATABASE,
    DB_USER, DB_PASSWORD, DB_TYPE
  );

  public static Future<BrokerConfig> load(final Vertx vertx) {
    var exposedKeys = new JsonArray();
    EXPOSED_ENVIRONMENT_VARIABLES.forEach(exposedKeys::add);
    LOG.debug("Fetch configuration for {}", exposedKeys.encode());
    // You can use: export KEY=VALUE
    var envStore = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject()
        .put("keys", exposedKeys));

    // You can use: System.setProperty("KEY", "VALUE"); ...
    var propertyStore = new ConfigStoreOptions()
      .setType("sys")
      .setConfig(new JsonObject()
        .put("cache", false));

    var yamlStore = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(new JsonObject()
        .put("path", CONFIG_FILE));

    var retriever = ConfigRetriever.create(vertx,
      new ConfigRetrieverOptions()
        // Order defines overload rules
        .addStore(yamlStore)
        .addStore(envStore)
        .addStore(propertyStore));
    return retriever.getConfig()
      .map(BrokerConfig::from);
  }
}
