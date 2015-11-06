package adventure.rest;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Event;
import adventure.entity.Race;
import adventure.persistence.EventDAO;
import adventure.persistence.RaceDAO;
import adventure.rest.data.CityData;
import adventure.rest.data.EventData;
import adventure.rest.data.RaceData;
import adventure.rest.data.StateData;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.util.Cache;

@Path("event")
public class EventREST {

	@GET
	@Path("{slug: [\\w\\d_\\-/]+}")
	@Cache("max-age=28800")
	@Produces("application/json")
	public EventData load(@PathParam("slug") String slug) throws Exception {
		EventData data = new EventData();
		Event event = loadEventDetails(slug);

		data.id = event.getId();
		data.name = event.getName();
		data.description = event.getDescription();
		data.slug = event.getSlug();
		data.site = event.getSite();
		data.races = new ArrayList<RaceData>();

		for (Race race : RaceDAO.getInstance().findForEvent(event)) {
			RaceData raceData = new RaceData();
			raceData.id = race.getId();
			raceData.name = race.getName();
			raceData.description = race.getDescription();
			raceData.beginning = race.getBeginning();
			raceData.end = race.getEnd();
			raceData.city = new CityData();
			raceData.city.id = race.getCity().getId();
			raceData.city.name = race.getCity().getName();
			raceData.city.state = new StateData();
			raceData.city.state.id = race.getCity().getState().getId();
			raceData.city.state.name = race.getCity().getState().getName();
			raceData.city.state.abbreviation = race.getCity().getState().getAbbreviation();
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
