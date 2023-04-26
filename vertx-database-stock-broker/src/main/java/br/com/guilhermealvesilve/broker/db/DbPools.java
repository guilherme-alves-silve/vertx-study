package br.com.guilhermealvesilve.broker.db;

import br.com.guilhermealvesilve.broker.config.BrokerConfig;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public class DbPools {

  public static Pool createPool(final Vertx vertx,
                                final BrokerConfig config) {
    var type = config.dbConfig().type();
    return switch (type) {
      case POSTGRESQL -> createPgPool(vertx, config);
      case MYSQL -> createMySQLPool(vertx, config);
      default -> throw new IllegalArgumentException("Invalid db type: " + type);
    };
  }

  private static MySQLPool createMySQLPool(final Vertx vertx, final BrokerConfig config) {
    // One pool for each Rest api verticle
    final var connectOptions = new MySQLConnectOptions()
      .setHost(config.dbConfig().host())
      .setPort(config.dbConfig().port())
      .setDatabase(config.dbConfig().database())
      .setUser(config.dbConfig().user())
      .setPassword(config.dbConfig().password());
    final var poolOptions = new PoolOptions()
      .setMaxSize(4);
    return MySQLPool.pool(vertx, connectOptions, poolOptions);
  }

  private static Pool createPgPool(final Vertx vertx, final BrokerConfig config) {
    // One pool for each Rest api verticle
    final var connectOptions = new PgConnectOptions()
      .setHost(config.dbConfig().host())
      .setPort(config.dbConfig().port())
      .setDatabase(config.dbConfig().database())
      .setUser(config.dbConfig().user())
      .setPassword(config.dbConfig().password());
    final var poolOptions = new PoolOptions()
      .setMaxSize(4);
    return PgPool.pool(vertx, connectOptions, poolOptions);
  }
}
