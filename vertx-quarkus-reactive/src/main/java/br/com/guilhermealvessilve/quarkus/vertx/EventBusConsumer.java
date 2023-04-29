package br.com.guilhermealvessilve.quarkus.vertx;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

import static br.com.guilhermealvessilve.quarkus.vertx.PeriodicUserFetcher.ADDRESS;

@Slf4j
@ApplicationScoped
public class EventBusConsumer extends AbstractVerticle {

    @Override
    public Uni<Void> asyncStart() {
        vertx.eventBus()
                .<JsonArray>consumer(ADDRESS, message -> {
                    LOG.info("Consumed from Event Bus: {}", message.body());
                });
        return Uni.createFrom().voidItem();
    }
}
