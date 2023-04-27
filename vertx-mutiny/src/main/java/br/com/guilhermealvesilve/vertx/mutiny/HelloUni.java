package br.com.guilhermealvesilve.vertx.mutiny;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloUni {

  public static void main(String[] args) {
    Uni.createFrom().item("Hello")
      .onItem().transform(item -> item + " Mutiny")
      .onItem().transform(String::toUpperCase)
      .subscribe().with(item -> LOG.info("Item: {}", item));

    System.out.println();

    Uni.createFrom().item("Ignored due to failure")
      .onItem().castTo(Integer.class)
      .subscribe().with(
        item -> LOG.info("Item: {}", item),
        error -> LOG.error("Failed with: ", error)
      );
  }
}
