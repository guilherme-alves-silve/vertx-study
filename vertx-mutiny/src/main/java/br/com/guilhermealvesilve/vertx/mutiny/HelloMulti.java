package br.com.guilhermealvesilve.vertx.mutiny;

import io.smallrye.mutiny.Multi;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

@Slf4j
public class HelloMulti {

  public static void main(String[] args) {
    Multi.createFrom().items(IntStream.rangeClosed(0, 10).boxed())
      .onItem().transform(value -> value * 2)
      .onItem().transform(String::valueOf)
      .subscribe().with(LOG::info);

    System.out.println();

    Multi.createFrom().items(IntStream.rangeClosed(0, 10).boxed())
      .onItem().transform(value -> value * 2)
      .onItem().transform(String::valueOf)
      .select().last(2)
      .subscribe().with(LOG::info);

    System.out.println();

    Multi.createFrom().items(IntStream.rangeClosed(0, 10).boxed())
      .onItem().transform(value -> value * 2)
      .onItem().transform(String::valueOf)
      .select().first(4)
      .subscribe().with(LOG::info);

    System.out.println();

    Multi.createFrom().items(IntStream.rangeClosed(0, 10).boxed())
      .onItem().transform(value -> value / 0) // on purpose
      .onItem().transform(String::valueOf)
      .onFailure().recoverWithItem("fallback")
      .select().last(2)
      .subscribe().with(LOG::info);

    System.out.println();

    Multi.createFrom().items(IntStream.rangeClosed(0, 10).boxed())
      .onItem().transform(value -> value / 0) // on purpose
      .onItem().transform(String::valueOf)
      .onFailure().invoke(error -> LOG.error("Transformation failed with: {}", error.getMessage()))
      .select().last(2)
      .subscribe().with(
        LOG::info,
        error -> LOG.error("Failed with: {}", error.getMessage())
      );
  }
}
