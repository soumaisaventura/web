package adventure.client;

import adventure.entity.User;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("auth")
@Consumes(APPLICATION_JSON)
public interface AuthClient {

    @GET
    @LoggedIn
    public User getAuthenticatedUser();

    @POST
    public Response login(Credentials credentials);

    @DELETE
    public void logout();
}
