package br.com.fbca.rest;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;

@Path("user")
public class UserREST {

	@Inject
	private SecurityContext securityContext;

	@GET
	@LoggedIn
	@Produces("application/json")
	public Principal getLoggedInUser() {
		return securityContext.getUser();
	}
}
