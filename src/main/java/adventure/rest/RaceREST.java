package adventure.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Category;
import adventure.entity.Race;
import adventure.persistence.CategoryDAO;
import adventure.persistence.RaceDAO;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.util.Beans;

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
	@Path("{id}/categories")
	@Produces("application/json")
	public List<CategoryData> findCategories(@PathParam("id") Long id) throws Exception {
		List<CategoryData> result = new ArrayList<CategoryData>();
		Race race = loadRace(id);

		for (Category category : Beans.getReference(CategoryDAO.class).find(race)) {
			CategoryData data = new CategoryData();
			data.id = category.getId();
			data.name = category.getName() + " " + category.getCourse().getLength() + "Km";
			data.description = category.getDescription();
			data.members = category.getMembers();
			data.course = category.getCourse().getId();

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
