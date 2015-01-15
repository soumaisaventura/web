package adventure.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import adventure.entity.User;
import br.gov.frameworkdemoiselle.security.LoggedIn;

@Path("user")
public class UserREST {

	@GET
	@LoggedIn
	@Produces("application/json")
	public User getLoggedInUser() {
		return User.getLoggedIn();
	}
}
