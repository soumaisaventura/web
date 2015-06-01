package adventure.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Race;
import adventure.entity.RaceAnalytic;
import adventure.entity.User;
import adventure.persistence.RaceAnalyticDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.security.LoggedIn;

@Path("race/{id}/analytics")
public class RaceAnalyticsREST {

	@GET
	@LoggedIn
	@Path("category")
	@Produces("application/json")
	public List<RaceAnalytic> getCategories(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);
		checkPermission(race);
		return RaceAnalyticDAO.getInstance().getRegistrationByCategory(race);
	}

	@GET
	@LoggedIn
	@Path("course")
	@Produces("application/json")
	public List<RaceAnalytic> getCourses(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);
		checkPermission(race);
		return RaceAnalyticDAO.getInstance().getRegistrationByCourse(race);
	}

	@GET
	@LoggedIn
	@Path("status")
	@Produces("application/json")
	public List<RaceAnalytic> getStatus(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);
		checkPermission(race);
		return RaceAnalyticDAO.getInstance().getRegistrationByStatus(race);
	}

	@GET
	@LoggedIn
	@Path("location")
	@Produces("application/json")
	public List<RaceAnalytic> getCities(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);
		checkPermission(race);
		return RaceAnalyticDAO.getInstance().getRegistrationByCity(race);
	}

	@GET
	@LoggedIn
	@Path("tshirt")
	@Produces("application/json")
	public List<RaceAnalytic> getTshirts(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);
		checkPermission(race);
		return RaceAnalyticDAO.getInstance().getRegistrationByTshirt(race);
	}

	private Race loadRace(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().loadJustId(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private void checkPermission(Race race) throws ForbiddenException {
		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}
	}
}
