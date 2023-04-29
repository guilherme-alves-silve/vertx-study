package br.com.guilhermealvessilve.quarkus;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Slf4j
@Path("/users")
public class UserResource {

    @GET
    public Uni<List<User>> get() {
        LOG.info("Get all users...");
        return User.listAll(Sort.by("id"));
    }

    @GET
    @Path("/{id}")
    public Uni<User> getById(Long id) {
        LOG.info("Get by id: {}", id);
        return User.findById(id);
    }

    @POST
    public Uni<Response> create(User user) {
        LOG.info("Create: {}", user);
        return Panache.<User>withTransaction(user::persist)
            .onItem().transform(insertedUser ->
                Response.created(URI.create("/users/" + insertedUser.id))
                        .build());
    }
}
