package adventure.client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import adventure.entity.Account;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;

@Path("auth")
@Consumes(APPLICATION_JSON)
public interface AuthClient {

	@GET
	@LoggedIn
	public Account getAuthenticatedUser();

	@POST
	public Response login(Credentials credentials);

	@DELETE
	public void logout();
}
