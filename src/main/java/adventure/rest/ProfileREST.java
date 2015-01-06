package adventure.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import adventure.entity.Profile;
import adventure.persistence.ProfileDAO;
import adventure.security.User;
import br.gov.frameworkdemoiselle.security.LoggedIn;

@Path("profile")
public class ProfileREST {

	@Inject
	private ProfileDAO dao;

	@GET
	@LoggedIn
	@Produces("application/json")
	public Profile obter() throws Exception {
		Profile profile = dao.load(User.getLoggedIn().getEmail());
		profile.setAccount(null);
		
		return profile;
	}
}
