package adventure.rest;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;
import static adventure.util.Constants.RACE_SLUG_PATTERN;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.entity.Category;
import adventure.entity.Race;
import adventure.entity.RegistrationPeriod;
import adventure.entity.User;
import adventure.persistence.CategoryDAO;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.UserDAO;
import adventure.rest.data.CategoryData;
import adventure.rest.data.CityData;
import adventure.rest.data.EventData;
import adventure.rest.data.LocationData;
import adventure.rest.data.PeriodData;
import adventure.rest.data.RaceData;
import adventure.rest.data.UserData;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Cache;

@Path("event/{eventSlug: " + EVENT_SLUG_PATTERN + "}/{raceSlug: " + RACE_SLUG_PATTERN + "}")
public class RaceREST {

	@GET
	@Path("summary")
	@Cache("max-age=28800")
	@Produces("application/json")
	public RaceData loadSummary(@PathParam("raceSlug") String raceSlug, @PathParam("eventSlug") String eventSlug)
			throws Exception {
		RaceData data = new RaceData();
		Race race = loadRaceDetails(raceSlug, eventSlug);

		data.id = race.getSlug();
		data.internalId = race.getId();
		data.name = race.getName();
		data.description = race.getDescription();
		data.status = race.getStatus().getName();

		data.period = new PeriodData();
		data.period.beginning = race.getPeriod().getBeginning();
		data.period.end = race.getPeriod().getEnd();

		data.event = new EventData();
		data.event.id = race.getEvent().getSlug();
		data.event.internalId = race.getEvent().getId();
		data.event.name = race.getEvent().getName();
		data.event.site = race.getEvent().getSite();

		data.event.location = new LocationData();
		data.event.location.city = new CityData();
		data.event.location.city.id = race.getEvent().getCity().getId();
		data.event.location.city.name = race.getEvent().getCity().getName();
		data.event.location.city.state = race.getEvent().getCity().getState().getAbbreviation();

		return data;
	}

	@GET
	@Path("categories")
	@Cache("max-age=28800")
	@Produces("application/json")
	public List<CategoryData> findCategories(@PathParam("raceSlug") String raceSlug,
			@PathParam("eventSlug") String eventSlug) throws Exception {
		List<CategoryData> result = new ArrayList<CategoryData>();
		Race race = loadRaceDetails(raceSlug, eventSlug);

		for (Category category : CategoryDAO.getInstance().find(race)) {
			CategoryData categoryData = new CategoryData();
			categoryData.id = category.getId();
			categoryData.name = category.getName();
			categoryData.description = category.getDescription();
			categoryData.teamSize = category.getTeamSize();
			categoryData.minMaleMembers = category.getMinMaleMembers();
			categoryData.minFemaleMembers = category.getMinFemaleMembers();
			result.add(categoryData);
		}

		return result.isEmpty() ? null : result;
	}

	@GET
	@Path("order")
	@Cache("max-age=28800")
	@Produces("application/json")
	public List<UserData> getOrder(@PathParam("raceSlug") String raceSlug, @PathParam("eventSlug") String eventSlug,
			@QueryParam("users_ids") List<Integer> users) throws Exception {
		Race race = loadRaceDetails(raceSlug, eventSlug);

		RegistrationPeriod period = PeriodDAO.getInstance().loadCurrent(race);
		List<UserData> result = new ArrayList<UserData>();

		if (users.isEmpty()) {
			throw new UnprocessableEntityException().addViolation("users", "parâmetro obrigatório");

		} else {
			for (Integer userId : users) {
				User user = UserDAO.getInstance().loadBasics(userId);

				if (user == null) {
					throw new UnprocessableEntityException().addViolation("users", "usuário inválido");
				} else {
					UserData row = new UserData();
					row.id = user.getId();
					row.name = user.getProfile().getName();
					row.racePrice = period.getPrice();

					result.add(row);
				}
			}
		}

		period.setRace(null);
		return result;
	}

	private Race loadRaceDetails(String raceSlug, String eventSlug) throws Exception {
		Race result = RaceDAO.getInstance().loadForDetail(raceSlug, eventSlug);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}
}
