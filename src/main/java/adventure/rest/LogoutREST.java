package adventure.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;

@Path("logout")
public class LogoutREST {

	@POST
	@LoggedIn
	public void logout() {
		Beans.getReference(SecurityContext.class).logout();
	}
}
