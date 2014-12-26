package br.com.fbca.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.com.fbca.entity.User;
import br.com.fbca.persistence.UserDAO;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Path("user")
public class UserREST {

	@Inject
	private UserDAO dao;

	@GET
	@LoggedIn
	@Produces("application/json")
	public User getLoggedInUser() {
		SecurityContext securityContext = Beans.getReference(SecurityContext.class);

		User user = (User) securityContext.getUser();
		user.setPassword(null);

		return user;
	}

	@GET
	@Path("all")
	@Transactional
	@Produces("application/json")
	public List<User> getAll() {
		return dao.findAll();
	}

	@GET
	@Path("{id}")
	@Transactional
	@Produces("application/json")
	public User getAll(@PathParam("id") Long id) {
		return dao.load(id);
	}

	@DELETE
	@Path("clear")
	@Transactional
	@Produces("application/json")
	public void delete() {
		for (User user : dao.findAll()) {
			dao.delete(user.getId());
		}
	}
}
