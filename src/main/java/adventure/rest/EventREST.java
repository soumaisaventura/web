package adventure.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Event;
import adventure.entity.Period;
import adventure.entity.Race;
import adventure.persistence.EventDAO;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceDAO;
import adventure.rest.data.CityData;
import adventure.rest.data.EventData;
import adventure.rest.data.PeriodData;
import adventure.rest.data.RaceData;
import adventure.rest.data.SportData;
import adventure.rest.data.StateData;
import br.gov.frameworkdemoiselle.NotFoundException;

@Path("event")
public class EventREST {

	@GET
	@Path("{slug: [\\w\\d_\\-/]+}")
	// @Cache("max-age=28800")
	@Produces("application/json")
	public EventData load(@PathParam("slug") String slug) throws Exception {
		EventData data = new EventData();
		Event event = loadEventDetails(slug);

		data.id = event.getSlug();
		data.name = event.getName();
		data.description = event.getDescription();
		data.site = event.getSite();

		data.races = new ArrayList<RaceData>();
		for (Race race : RaceDAO.getInstance().findForEvent(event)) {
			RaceData raceData = new RaceData();
			raceData.id = race.getSlug();
			raceData.name = race.getName();
			raceData.description = race.getDescription();

			raceData.sport = new SportData();
			raceData.sport.id = race.getSport().getAcronym();
			raceData.sport.name = race.getSport().getName();

			raceData.period = new PeriodData();
			raceData.period.beginning = race.getBeginning();
			raceData.period.end = race.getEnd();

			raceData.city = new CityData();
			raceData.city.name = race.getCity().getName();
			raceData.city.state = new StateData();
			raceData.city.state.id = race.getCity().getState().getAbbreviation();
			raceData.city.state.name = race.getCity().getState().getName();

			raceData.prices = new ArrayList<PeriodData>();
			for (Period period : PeriodDAO.getInstance().findForEvent(race)) {
				PeriodData periodData = new PeriodData();
				periodData.beginning = period.getBeginning();
				periodData.end = period.getEnd();
				periodData.price = period.getPrice();
				raceData.prices.add(periodData);
			}

			data.races.add(raceData);
		}

		return data;
	}

	private Event loadEventDetails(String slug) throws Exception {
		Event result = EventDAO.getInstance().loadForDetail(slug);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}
}
