package br.com.fbca.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import br.com.fbca.entity.Race;
import br.com.fbca.persistence.RaceDAO;

@Path("race")
public class RaceREST {

	@Inject
	private RaceDAO raceDAO;

	@GET
	@Path("next")
	@Produces("application/json")
	public List<Race> nextRaces() throws Exception {
		List<Race> result = raceDAO.findNext();
		return result.isEmpty() ? null : result;
	}
}
