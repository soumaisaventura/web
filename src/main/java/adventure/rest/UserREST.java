package adventure.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
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
	@Path("search")
	@Produces("application/json")
	public List<User> search(@QueryParam("q") String q, @QueryParam("excludes") List<Integer> excludes)
			throws Exception {
		validate(q);
		return UserDAO.getInstance().search(q, excludes);
	}

	private void validate(String q) throws Exception {
		if (Strings.isEmpty(q)) {
			throw new UnprocessableEntityException().addViolation("q", "parâmetro obrigatório");
		} else if (q.length() < 3) {
			throw new UnprocessableEntityException().addViolation("q", "deve possuir 3 ou mais caracteres");
		}
	}
}
