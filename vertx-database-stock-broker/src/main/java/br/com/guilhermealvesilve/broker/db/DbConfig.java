package br.com.guilhermealvesilve.broker.db;

import lombok.Builder;

import java.util.StringJoiner;

public record DbConfig(String host,
                       int port,
                       String database,
                       String user,
                       String password) {

  @Builder
  public DbConfig { }

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
