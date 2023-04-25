package br.com.guilhermealvesilve.broker.db;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class FlywayMigration {

  public static Future<Void> migrate(Vertx vertx, DbConfig dbConfig) {
    LOG.debug("DB Config: {}", dbConfig);
    return vertx.<Void>executeBlocking(promise -> {
      //Flyway migration is blocking => uses JDBC
      execute(dbConfig);
      promise.complete();
    })
    .onFailure(error -> LOG.error("Failed to migrate db schema with error: {}", error));
  }

  private static void execute(DbConfig dbConfig) {
    var jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s",
      dbConfig.host(), dbConfig.port(), dbConfig.database());

    LOG.debug("Migrating DB schema using jdbc url: {}", jdbcUrl);

    final var flyway = Flyway.configure()
      .dataSource(jdbcUrl, dbConfig.user(), dbConfig.password())
      .schemas("broker")
      .defaultSchema("broker")
      .load();

    Optional.ofNullable(flyway.info().current())
        .ifPresent(info -> LOG.info("db schema is at version: {}", info.getVersion()));

    var pendingMigrations = flyway.info().pending();
    LOG.debug("Pending migrations are: {}", getToStringMigrations(pendingMigrations));

    flyway.migrate();
  }

  private static String getToStringMigrations(final MigrationInfo[] pending) {
    if (null == pending) return "[]";
    return Arrays.stream(pending)
      .map(each -> each.getVersion() + " - " + each.getDescription())
      .collect(Collectors.joining(",", "[", "]"));
  }
}
