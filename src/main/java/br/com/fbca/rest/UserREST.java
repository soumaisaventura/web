package br.com.fbca.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.fbca.entity.User;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;

@Path("user")
public class UserREST {

	@Inject
	private SecurityContext securityContext;

	@GET
	@LoggedIn
	@Produces("application/json")
	public User getLoggedInUser() {
		User user = (User) securityContext.getUser(); 
		user.setPassword(null);
		
		return user;
	}
}
