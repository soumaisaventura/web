package adventure.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import adventure.security.Passwords;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Path("temp")
public class TempREST {

	@POST
	@Transactional
	@Path("prepare")
	public void prepare(@Context UriInfo uriInfo) throws Exception {
		validate(uriInfo);

		UserDAO userDAO = UserDAO.getInstance();
		for (User user : userDAO.findAll()) {
			if (!user.getAdmin() && !user.getEmail().startsWith("test_")) {
				user.setEmail("test_" + user.getEmail());
			}
			user.setPassword(Passwords.hash("123", user.getEmail()));
			userDAO.update(user);
		}
	}

	private void validate(UriInfo uriInfo) throws Exception {
		if (uriInfo.getBaseUri().toString().contains("com.br")) {
			throw new ForbiddenException().addViolation("Em produção não pode!");
		}
	}
}
