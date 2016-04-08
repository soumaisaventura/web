package adventure.rest;

import adventure.entity.Event;
import adventure.entity.EventAnalytic;
import adventure.entity.EventRegistrationStatusByDay;
import adventure.entity.User;
import adventure.persistence.EventAnalyticDAO;
import adventure.persistence.EventDAO;
import adventure.persistence.UserDAO;
import adventure.rest.data.EventRegistrationStatusByDayData;
import adventure.rest.data.EventRegistrationStatusByDayData.Status;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.security.LoggedIn;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;

@Path("event/{slug: " + EVENT_SLUG_PATTERN + "}/analytics")
public class EventAnalyticsREST {

	@GET
	@LoggedIn
	@Path("gender")
	@Produces("application/json")
	public List<EventAnalytic> getGenders(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		
		List<EventAnalytic> result = new ArrayList<EventAnalytic>();
		result = EventAnalyticDAO.getInstance().getGenderQuantity(event);
		
		return result.isEmpty() ? null : result;
	}

	@GET
	@LoggedIn
	@Path("amountraised")
	@Produces("application/json")
	public List<EventAnalytic> getAmountRaised(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		return EventAnalyticDAO.getInstance().getAmountRaised(event);
	}

	@GET
	@LoggedIn
	@Path("discount")
	@Produces("application/json")
	public List<EventAnalytic> getAmountDiscounted(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		return EventAnalyticDAO.getInstance().getAmountDiscounted(event);
	}

	@GET
	@LoggedIn
	@Path("agegroup")
	@Produces("application/json")
	public List<EventAnalytic> gerRegistrationByAgeGroup(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		return EventAnalyticDAO.getInstance().gerRegistrationByAgeGroup(event);
	}

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
		return EventAnalyticDAO.getInstance().getRegistrationByRace(event);
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
	@Path("status/day")
	@Produces("application/json")
	public List<EventRegistrationStatusByDayData> getStatusByDay(@PathParam("slug") String slug) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);
		List<EventRegistrationStatusByDayData> result = new ArrayList<EventRegistrationStatusByDayData>();

		int pendent = 0;
		int confimed = 0;
		int cancelled = 0;
		Date now = new Date();

		for (EventRegistrationStatusByDay persisted : EventAnalyticDAO.getInstance().getEventRegistrationStatusByDay(
				event)) {
			pendent += persisted.getPendentCount();
			confimed += persisted.getConfirmedCount();
			cancelled += persisted.getCancelledCount();

			EventRegistrationStatusByDayData data = new EventRegistrationStatusByDayData();
			data.date = persisted.getDate();
			data.status = new Status();

			if (Dates.beforeOrSame(data.date, now)) {
				data.status.pendent = pendent;
				data.status.confirmed = confimed;
				data.status.cancelled = cancelled;
			}

			result.add(data);
		}

		return result.isEmpty() ? null : result;
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
