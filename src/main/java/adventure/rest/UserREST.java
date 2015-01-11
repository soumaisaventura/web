package adventure.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.persistence.UserDAO;
import adventure.security.User;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("user")
public class UserREST {

	@GET
	@LoggedIn
	@Produces("application/json")
	public User getLoggedInUser() {
		return User.getLoggedIn();
	}

	@GET
	@LoggedIn
	@Path("search")
	@Produces("application/json")
	public List<User> search(@QueryParam("q") String q, @QueryParam("excludes") List<Long> excludes) throws Exception {
		if (Strings.isEmpty(q)) {
			throw new UnprocessableEntityException().addViolation("q", "parâmetro obrigatório");
		} else if (q.length() < 3) {
			throw new UnprocessableEntityException().addViolation("q", "deve possuir 3 ou mais caracteres");
		}

		return Beans.getReference(UserDAO.class).search(q, excludes);
	}

	//
	// @GET
	// @Path("{id}")
	// @Transactional
	// @Produces("application/json")
	// public Account getAll(@PathParam("id") Long id) {
	// return dao.load(id);
	// }
}
