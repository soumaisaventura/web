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

import adventure.entity.Account;
import adventure.entity.Category;
import adventure.entity.Course;
import adventure.entity.Gender;
import adventure.entity.Race;
import adventure.entity.Register;
import adventure.persistence.AccountDAO;
import adventure.persistence.CategoryDAO;
import adventure.persistence.CourseDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.RegisterDAO;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("race/{id}/register")
public class RegisterREST {

	@Inject
	private RegisterDAO dao;

	@POST
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	@Produces("application/json")
	public Long submit(RegisterData data, @PathParam("id") Long id) throws Exception {
		Race race = loadRace(id);
		Course course = loadCourse(data.course);
		Category category = loadCategory(data.category);
		List<Account> members = loadMembers(data.members);

		Register register = new Register();
		register.setRace(race);
		register.setCourse(course);
		register.setCategory(category);
		register.setTeamName(data.teamName);

		validate(register);
		validate(category, members);

		return dao.insert(register).getId();
	}

	private Race loadRace(Long id) throws Exception {
		Race result = Beans.getReference(RaceDAO.class).load(id);

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

	private List<Account> loadMembers(List<Long> ids) throws Exception {
		List<Account> result = new ArrayList<Account>();
		UnprocessableEntityException exception = new UnprocessableEntityException();

		for (Long id : ids) {
			Account account = Beans.getReference(AccountDAO.class).load(id);

			if (account == null) {
				exception.addViolation("members", "usuário inválido");
			} else {
				result.add(account);
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}

		return result;
	}

	private void validate(Register register) throws Exception {
		List<Category> available = new ArrayList<Category>();
		for (Category aux : Beans.getReference(CategoryDAO.class).find(register.getRace())) {
			if (aux.equals(register.getCategory())) {
				available.add(aux);
			}
		}

		if (available.isEmpty()) {
			throw new UnprocessableEntityException().addViolation("category", "indisponível para esta prova");
		}

		boolean found = false;
		for (Category aux : available) {
			if (aux.getCourse().equals(register.getCourse())) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new UnprocessableEntityException().addViolation("course", "indisponível para esta categoria");
		}
	}

	private void validate(Category category, List<Account> members) throws Exception {
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

	private int count(List<Account> members, Gender gender) {
		int result = 0;

		for (Account account : members) {
			if (account.getProfile().getGender() == gender) {
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
