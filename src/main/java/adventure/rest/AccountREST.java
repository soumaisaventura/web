package adventure.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Account;
import adventure.persistence.AccountDAO;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Path("user")
public class AccountREST {

	@Inject
	private AccountDAO dao;

	@GET
	@LoggedIn
	@Produces("application/json")
	public Account getLoggedInUser() {
		SecurityContext securityContext = Beans.getReference(SecurityContext.class);

		Account account = (Account) securityContext.getUser();
		account.setPassword(null);

		return account;
	}

	@GET
	@Path("all")
	@Transactional
	@Produces("application/json")
	public List<Account> getAll() {
		return dao.findAll();
	}

	@GET
	@Path("{id}")
	@Transactional
	@Produces("application/json")
	public Account getAll(@PathParam("id") Long id) {
		return dao.load(id);
	}
}
