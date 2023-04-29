package br.com.guilhermealvessilve.quarkus.vertx;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;

@Slf4j
@ApplicationScoped
public class PeriodicUserFetcher extends AbstractVerticle {

    public static final String ADDRESS = PeriodicUserFetcher.class.getName();

    @Override
    public Uni<Void> asyncStart() {

        var client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(8080));

        vertx.periodicStream(Duration.ofSeconds(5).toMillis())
                .toMulti()
                .subscribe()
                .with(item -> {
                    LOG.info("Fetch all users!");
                    client.get("/users").send()
                            .subscribe().with(result -> {
                               var body = result.bodyAsJsonArray();
                               LOG.info("All users from http response: {}", body);
                               vertx.eventBus().publish(ADDRESS, body);
                            });
                });
        return Uni.createFrom().voidItem();
    }
}
