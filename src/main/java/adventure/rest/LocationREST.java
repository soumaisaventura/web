package adventure.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.City;
import adventure.entity.State;
import adventure.persistence.CityDAO;
import adventure.persistence.StateDAO;
import br.gov.frameworkdemoiselle.NotFoundException;

@Path("location")
public class LocationREST {

	@GET
	@Path("uf")
	@Produces("application/json")
	public List<State> loadStates() throws Exception {
		return StateDAO.getInstance().findAll();
	}

	@GET
	@Path("uf/{abbreviation}/cities")
	@Produces("application/json")
	public List<CityData> searchCity(@PathParam("abbreviation") String abbreviation) throws Exception {
		List<CityData> result = new ArrayList<CityData>();
		CityData data;

		State state = loadState(abbreviation);

		for (City city : CityDAO.getInstance().find(state)) {
			data = new CityData();
			data.id = city.getId();
			data.name = city.getName();
			data.state = null;

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}


	private State loadState(String abbreviation) throws Exception {
		State result = StateDAO.getInstance().load(abbreviation);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}
	
	public static class CityData {

		@NotNull(message = "{adventure.validation.constraints.NotNullCityId.message}")
		public Integer id;

		public String name;

		public String state;

		public String country;
	}
}
