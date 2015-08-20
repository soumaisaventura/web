package adventure.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.entity.City;
import adventure.entity.State;
import adventure.persistence.CityDAO;
import adventure.persistence.StateDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("location")
public class LocationREST {

	
	@GET
	@Path("uf")
	@Produces("application/json")
	public List<State> loadStates() throws Exception {
		return StateDAO.getInstance().findAll();
	}
	
	
	@GET
	@Path("city")
	@Produces("application/json")
	public List<CityData> searchCity(@QueryParam("stateId") int stateId) throws Exception {
		List<CityData> result = new ArrayList<CityData>();
		CityData data;

		for (City city : CityDAO.getInstance().loadByState(stateId)) {
			data = new CityData();
			data.id = city.getId();
			data.name = city.getName();
			data.state = city.getState().getAbbreviation();

			// if (!"brasil".equals(city.getState().getCountry().getName().toLowerCase())) {
			data.country = city.getState().getCountry().getName();
			// }

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	public static class CityData {

		@NotNull(message = "{adventure.validation.constraints.NotNullCityId.message}")
		public Integer id;

		public String name;

		public String state;

		public String country;
	}
}
