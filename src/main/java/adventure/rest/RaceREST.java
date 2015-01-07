package adventure.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import adventure.entity.Race;
import adventure.persistence.RaceDAO;

@Path("race")
public class RaceREST {

	@Inject
	private RaceDAO raceDAO;

	@GET
	@Path("next")
	@Produces("application/json")
	public List<Race> next() throws Exception {
		List<Race> result = raceDAO.findNext();
		return result.isEmpty() ? null : result;
	}
}
