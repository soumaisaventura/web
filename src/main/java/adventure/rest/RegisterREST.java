package adventure.rest;

import static adventure.entity.Gender.FEMALE;
import static adventure.entity.Gender.MALE;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Category;
import adventure.entity.Course;
import adventure.entity.Gender;
import adventure.entity.Profile;
import adventure.entity.Race;
import adventure.persistence.CategoryDAO;
import adventure.persistence.CourseDAO;
import adventure.persistence.ProfileDAO;
import adventure.persistence.RaceDAO;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("race/{id}/register")
public class RegisterREST {

	@Inject
	private RaceDAO dao;

	@POST
	@ValidatePayload
	@Consumes("application/json")
	@Produces("application/json")
	public Long submit(RegisterData data, @PathParam("id") Long id) throws Exception {
		Race race = loadRace(id);

		Category category = loadCategory(data.category);
		List<Profile> members = loadMembers(data.members);

		List<Category> available = new ArrayList<Category>();
		for (Category aux : Beans.getReference(CategoryDAO.class).find(race)) {
			if (aux.equals(category)) {
				available.add(aux);
			}
		}

		if (available.isEmpty()) {
			throw new UnprocessableEntityException().addViolation("category", "indisponível para esta prova");
		}

		boolean found = false;
		Course course = loadCourse(data.course);
		for (Category aux : available) {
			if (aux.getCourse().equals(course)) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new UnprocessableEntityException().addViolation("course", "indisponível para esta categoria");
		}

		validate(category, members);

		return null;
	}

	private Race loadRace(Long id) throws Exception {
		Race result = dao.load(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Category loadCategory(Long id) throws Exception {
		Category result = Beans.getReference(CategoryDAO.class).load(id);

		if (result == null) {
			throw new UnprocessableEntityException().addViolation("category", "categoria inválida");
		}

		return result;
	}

	private Course loadCourse(Long id) throws Exception {
		Course result = Beans.getReference(CourseDAO.class).load(id);

		if (result == null) {
			throw new UnprocessableEntityException().addViolation("course", "percurso inválido");
		}

		return result;
	}

	private List<Profile> loadMembers(List<Long> ids) throws Exception {
		List<Profile> result = new ArrayList<Profile>();
		UnprocessableEntityException exception = new UnprocessableEntityException();

		for (Long id : ids) {
			Profile profile = Beans.getReference(ProfileDAO.class).load(id);

			if (profile == null) {
				exception.addViolation("course", "percurso inválido");
			} else {
				result.add(profile);
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}

		return result;
	}

	private void validate(Category category, List<Profile> members) throws Exception {
		int total = members.size();
		if (total > category.getMembers()) {
			throw new UnprocessableEntityException().addViolation("members", "tem muita gente");
		} else if (total < category.getMembers()) {
			throw new UnprocessableEntityException().addViolation("members", "equipe incompleta");
		}

		int male = count(members, MALE);
		if (category.getMinMaleMembers() != null && male < category.getMinMaleMembers()) {
			throw new UnprocessableEntityException().addViolation("members", "está faltando macho");
		}

		int female = count(members, FEMALE);
		if (category.getMinFemaleMembers() != null && female < category.getMinFemaleMembers()) {
			throw new UnprocessableEntityException().addViolation("members", "está faltando mulher");
		}
	}

	private int count(List<Profile> members, Gender gender) {
		int result = 0;

		for (Profile profile : members) {
			if (profile.getGender() == gender) {
				result++;
			}
		}

		return result;
	}

	public static class RegisterData {

		@NotEmpty
		public String teamName;

		@NotEmpty
		public Long category;

		@NotEmpty
		public Long course;

		@NotEmpty
		public List<Long> members;
	}
}
