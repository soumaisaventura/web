package adventure.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.persistence.RaceCategoryDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.UserDAO;
import adventure.security.User;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

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

	@GET
	@Path("{id}/users")
	@Produces("application/json")
	public List<User> search(@PathParam("id") Long id, @QueryParam("q") String q,
			@QueryParam("excludes") List<Long> excludes) throws Exception {
		Race race = loadRace(id);
		validate(q);
		return Beans.getReference(UserDAO.class).searchAvailable(race, q, excludes);
	}

	private void validate(String q) throws Exception {
		if (Strings.isEmpty(q)) {
			throw new UnprocessableEntityException().addViolation("q", "parâmetro obrigatório");
		} else if (q.length() < 3) {
			throw new UnprocessableEntityException().addViolation("q", "deve possuir 3 ou mais caracteres");
		}
	}

	@GET
	@Path("{id}/categories")
	@Produces("application/json")
	public List<CategoryData> findCategories(@PathParam("id") Long id) throws Exception {
		Race race = loadRace(id);
		List<CategoryData> result = new ArrayList<CategoryData>();

		for (RaceCategory raceCategory : Beans.getReference(RaceCategoryDAO.class).find(race)) {
			CategoryData data = new CategoryData();
			data.id = raceCategory.getCategory().getId();
			data.name = raceCategory.getCategory().getName() + " " + raceCategory.getCourse().getLength() + "Km";
			data.description = raceCategory.getCategory().getDescription();
			data.teamSize = raceCategory.getCategory().getTeamSize();
			data.course = raceCategory.getCourse().getId();

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	// @GET
	// @Path("{id}/bill")
	// @Produces("application/json")
	// public void getBill(@PathParam("id") Long id, @QueryParam("users") List<Long> users) throws Exception {
	// Race race = loadRace(id);
	//
	// if (users == null) {
	// throw new UnprocessableEntityException().addViolation("users", "parâmetro obrigatório");
	// }
	//
	// // raceDAO.
	// }

	private Race loadRace(Long id) throws Exception {
		Race result = raceDAO.loadJustId(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	public static class CategoryData {

		public Long id;

		public String name;

		public String description;

		public Integer teamSize;

		public Long course;
	}
}
