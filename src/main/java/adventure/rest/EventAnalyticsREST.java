package adventure.rest;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Event;
import adventure.entity.EventAnalytic;
import adventure.entity.User;
import adventure.persistence.EventAnalyticDAO;
import adventure.persistence.EventDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.security.LoggedIn;

@Path("event/{slug: " + EVENT_SLUG_PATTERN + "}/analytics")
public class EventAnalyticsREST {

	@GET
	@LoggedIn
	@Path("category")
	@Produces("application/json")
	public List<EventAnalytic> getCategories(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		return EventAnalyticDAO.getInstance().getRegistrationByCategory(event);
	}

	@GET
	@LoggedIn
	@Path("race")
	@Produces("application/json")
	public List<EventAnalytic> getRaces(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		return EventAnalyticDAO.getInstance().getRegistrationByCourse(event);
	}

	@GET
	@LoggedIn
	@Path("status")
	@Produces("application/json")
	public List<EventAnalytic> getStatus(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		List<EventAnalytic> result = EventAnalyticDAO.getInstance().getRegistrationByStatus(event);
		for (EventAnalytic analytic : result) {
			analytic.setLabel(analytic.getLabel().toLowerCase());
		}

		return result;
	}

	@GET
	@LoggedIn
	@Path("location")
	@Produces("application/json")
	public List<EventAnalytic> getCities(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		return EventAnalyticDAO.getInstance().getRegistrationByCity(event);
	}

	@GET
	@LoggedIn
	@Path("tshirt")
	@Produces("application/json")
	public List<EventAnalytic> getTshirts(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		return EventAnalyticDAO.getInstance().getRegistrationByTshirt(event);
	}

	private Event loadEvent(String slug) throws Exception {
		Event result = EventDAO.getInstance().load(slug);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private void checkPermission(Event event) throws ForbiddenException {
		List<User> organizers = UserDAO.getInstance().findOrganizers(event);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}
	}
}
