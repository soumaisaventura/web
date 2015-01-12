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
	private RaceDAO dao;

	@GET
	@Path("next")
	@Produces("application/json")
	public List<Race> next() throws Exception {
		List<Race> result = dao.findNext();
		return result.isEmpty() ? null : result;
	}

	@GET
	@Path("{id}/users")
	@Produces("application/json")
	public List<User> search(@PathParam("id") Long id, @QueryParam("q") String q,
			@QueryParam("excludes") List<Long> excludes) throws Exception {
		Race race = loadRace(id);
		validate(q);

		if (excludes == null) {
			excludes = new ArrayList<Long>();
		}
		excludes.add(User.getLoggedIn().getId());

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
		List<CategoryData> result = new ArrayList<CategoryData>();
		Race race = loadRace(id);

		for (RaceCategory raceCategory : Beans.getReference(RaceCategoryDAO.class).find(race)) {
			CategoryData data = new CategoryData();
			data.id = raceCategory.getCategory().getId();
			data.name = raceCategory.getCategory().getName() + " " + raceCategory.getCourse().getLength() + "Km";
			data.description = raceCategory.getCategory().getDescription();
			data.members = raceCategory.getCategory().getMembers();
			data.course = raceCategory.getCourse().getId();

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	private Race loadRace(Long id) throws Exception {
		Race result = dao.load(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	public static class CategoryData {

		public Long id;

		public String name;

		public String description;

		public Integer members;

		public Long course;
	}
}
