package br.com.guilhermealvessilve.quarkus;

import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Slf4j
@Path("/users")
public class UserResource {

    @GET
    public Uni<List<User>> get() {
        LOG.info("Get all users...");
        return null;
    }
}
