package adventure.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import adventure.entity.Profile;
import adventure.persistence.ProfileDAO;
import adventure.security.User;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Path("user")
public class UserREST {

	@GET
	@LoggedIn
	@Produces("application/json")
	public User getLoggedInUser() {
		return User.getLoggedIn();
	}

	@GET
	@Path("all")
	@Transactional
	@Produces("application/json")
	public List<User> getAll() {
		List<User> result = new ArrayList<User>();
		ProfileDAO profileDAO = Beans.getReference(ProfileDAO.class);

		for (Profile profile : profileDAO.findAll()) {
			User user = User.parse(profile);
			result.add(user);
		}

		return result.isEmpty() ? null : result;
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
