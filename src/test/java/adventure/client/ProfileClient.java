package adventure.client;

import adventure.entity.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("profile")
@Consumes(APPLICATION_JSON)
public interface ProfileClient {

    @GET
    @Path("/{id}")
    public User load(@PathParam("id") Long id);

    @GET
    @Path("/{email}")
    public User loadByEmail(@PathParam("email") String email);
}
