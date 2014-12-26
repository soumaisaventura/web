package br.com.fbca.rest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;

@Path("logout")
public class LogoutREST {

	@Inject
	private SecurityContext securityContext;

	@POST
	@LoggedIn
	public void logout() {
		securityContext.logout();
	}
}
