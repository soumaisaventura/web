package adventure.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.entity.City;
import adventure.persistence.CityDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("location")
public class LocationREST {

	@GET
	@Path("city")
	@Produces("application/json")
	public List<CityData> searchCity(@QueryParam("q") String q) throws Exception {
		validate(q);
		List<CityData> result = new ArrayList<CityData>();
		CityData data;

		for (City city : Beans.getReference(CityDAO.class).search(q)) {
			data = new CityData();
			data.id = city.getId();
			data.name = city.getName();
			data.state = city.getState().getName();

			// if (!"brasil".equals(city.getState().getCountry().getName().toLowerCase())) {
			data.country = city.getState().getCountry().getName();
			// }

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	private void validate(String q) throws Exception {
		if (Strings.isEmpty(q)) {
			throw new UnprocessableEntityException().addViolation("q", "parâmetro obrigatório");
		} else if (q.length() < 3) {
			throw new UnprocessableEntityException().addViolation("q", "deve possuir 3 ou mais caracteres");
		}
	}

	public static class CityData {

		public Long id;

		public String name;

		public String state;

		public String country;
	}
}
