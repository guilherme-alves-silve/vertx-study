package br.com.guilhermealvessilve.quarkus.vertx;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;

import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Slf4j
@Path("/vertx")
public class HelloVertxResource {

    private final Vertx vertx;
    private final WebClient client;

    @Inject
    public HelloVertxResource(Vertx vertx) {
        this.vertx = vertx;
        this.client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(8080));
    }

    @GET
    public Uni<JsonArray> get() {
        final var itens = new JsonArray();
        itens.add(new JsonObject().put("id", 1));
        itens.add(new JsonObject().put("id", 2));
        return Uni.createFrom()
                .item(itens);
    }

    @GET
    @Path("/users")
    public Uni<JsonArray> getFromUsersResource() {
        return client.get("/users").send()
                .onItem().transform(HttpResponse::bodyAsJsonArray);
    }
}
