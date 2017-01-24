package temp.client;

import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import core.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("auth")
@Consumes(APPLICATION_JSON)
public interface AuthClient {

    @GET
    @LoggedIn
    User getAuthenticatedUser();

    @POST
    Response login(Credentials credentials);

    @DELETE
    void logout();
}
