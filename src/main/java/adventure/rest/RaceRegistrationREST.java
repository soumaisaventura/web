package adventure.rest;

import static adventure.entity.Gender.FEMALE;
import static adventure.entity.Gender.MALE;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Category;
import adventure.entity.Gender;
import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.entity.Registration;
import adventure.entity.TeamFormation;
import adventure.entity.User;
import adventure.persistence.MailDAO;
import adventure.persistence.RaceCategoryDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.TeamFormationDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("race/{id}/registration")
public class RaceRegistrationREST {

	@POST
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Path("validate")
	@Consumes("application/json")
	@Produces("application/json")
	public void validate(RegistrationData data, @PathParam("id") Integer id) throws Exception {
		validateData(data, id);
	}

	@POST
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	@Produces("text/plain")
	public String submit(RegistrationData data, @PathParam("id") Integer id, @Context UriInfo uriInfo) throws Exception {
		ValidationResult validationResult = validateData(data, id);
		SubmitResult submitResult = submit(data, validationResult);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailDAO.getInstance().sendRegistrationCreation(submitResult.registration, submitResult.members, baseUri);
		return submitResult.registration.getFormattedId();
	}

	private SubmitResult submit(RegistrationData data, ValidationResult validationResult) {
		SubmitResult result = new SubmitResult();
		Registration registration = new Registration();
		registration.setTeamName(data.teamName);
		registration.setRaceCategory(validationResult.raceCategory);
		registration.setSubmitter(UserDAO.getInstance().load(User.getLoggedIn().getEmail()));
		result.registration = RegistrationDAO.getInstance().insert(registration);

		for (User member : validationResult.members) {
			User atachedMember = UserDAO.getInstance().load(member.getId());
			TeamFormation teamFormation = new TeamFormation(registration, atachedMember);
			TeamFormationDAO.getInstance().insert(teamFormation);
			result.members.add(atachedMember);
		}

		return result;
	}

	private ValidationResult validateData(RegistrationData data, Integer id) throws Exception {
		ValidationResult result = new ValidationResult();

		loadRace(id);
		result.raceCategory = loadRaceCategory(id, data.course, data.category);
		result.members = loadMembers(data.members);
		validate(result.raceCategory, result.members);

		return result;
	}

	private Race loadRace(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().loadForDetails(id);

		if (result == null) {
			throw new NotFoundException();
		}

		if (!result.getOpen()) {
			throw new UnprocessableEntityException().addViolation("Fora do período de inscrição.");
		}

		return result;
	}

	private RaceCategory loadRaceCategory(Integer raceId, Integer courseId, Integer categoryId) throws Exception {
		RaceCategory result = RaceCategoryDAO.getInstance().loadForRegistration(raceId, courseId, categoryId);

		if (result == null) {
			throw new UnprocessableEntityException().addViolation("category", "indisponível para esta prova");
		}

		return result;
	}

	private List<User> loadMembers(List<Integer> ids) throws Exception {
		List<User> result = new ArrayList<User>();
		UnprocessableEntityException exception = new UnprocessableEntityException();

		for (Integer id : ids) {
			User user = UserDAO.getInstance().loadBasics(id);

			if (user == null) {
				exception.addViolation("members", "usuário " + id + " inválido");
			} else if (result.contains(user)) {
				exception.addViolation("members", "usuário " + id + "duplicado");
			} else {
				result.add(user);
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}

		return result;
	}

	private void validate(RaceCategory raceCategory, List<User> members) throws Exception {
		int total = members.size();
		UnprocessableEntityException exception = new UnprocessableEntityException();
		Category category = raceCategory.getCategory();

		if (total > category.getTeamSize()) {
			exception.addViolation("members", "tem muita gente");
		} else if (total < category.getTeamSize()) {
			exception.addViolation("members", "equipe incompleta");
		}

		int male = count(members, MALE);
		if (category.getMinMaleMembers() != null && male < category.getMinMaleMembers()) {
			exception.addViolation("members", "está faltando macho");
		}

		int female = count(members, FEMALE);
		if (category.getMinFemaleMembers() != null && female < category.getMinFemaleMembers()) {
			exception.addViolation("members", "está faltando mulher");
		}

		for (User member : members) {
			TeamFormation formation = TeamFormationDAO.getInstance().loadForRegistrationSubmissionValidation(
					raceCategory.getRace(), member);

			if (formation != null) {
				exception.addViolation("members", member.getName() + " já faz parte da equipe "
						+ formation.getRegistration().getTeamName());
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}
	}

	private int count(List<User> members, Gender gender) {
		int result = 0;

		for (User user : members) {
			if (user.getProfile().getGender() == gender) {
				result++;
			}
		}

		return result;
	}

	private class ValidationResult {

		RaceCategory raceCategory;

		List<User> members = new ArrayList<User>();

	}

	private class SubmitResult {

		Registration registration;

		List<User> members = new ArrayList<User>();

	}

	public static class RegistrationData {

		@NotEmpty
		public String teamName;

		@NotNull
		public Integer category;

		@NotNull
		public Integer course;

		@NotEmpty
		public List<Integer> members;
	}
}