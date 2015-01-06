package adventure.rest_;

import java.util.List;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Account;
import adventure.persistence.AccountDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Path("profile")
public class ProfileREST {

	@Inject
	private AccountDAO dao;

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Account load(@PathParam("id") Long id) {
		return dao.load(id);
	}

	@GET
	@Path("/{email}")
	@Produces("application/json")
	public Account loadByEmail(@PathParam("email") String email) {
		return dao.load(email);
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void delete(@PathParam("id") Long id) {
		dao.delete(id);
	}

	@GET
	@Produces("application/json")
	public List<Account> search() throws NamingException {
		return dao.findAll();
	}
}
