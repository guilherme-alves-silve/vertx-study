package br.com.guilhermealvesilve.broker.db;

import java.util.StringJoiner;

public record DbConfig(String host,
                       int port,
                       String database,
                       String user,
                       String password) {

  public static DbConfig defaultConfig() {
    return new DbConfig("localhost", 5432,
      "vertx-stock-broker", "postgres", "secret");
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DbConfig.class.getSimpleName() + "[", "]")
      .add("host='" + host + "'")
      .add("port=" + port)
      .add("database='" + database + "'")
      .add("user='" + user + "'")
      .toString();
  }
}
