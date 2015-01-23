package adventure.rest;

import static adventure.entity.GenderType.FEMALE;
import static adventure.entity.GenderType.MALE;
import static adventure.entity.StatusType.PENDENT;

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
import adventure.entity.GenderType;
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
import br.gov.frameworkdemoiselle.util.Strings;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("race/{id}/registration")
public class RaceRegistrationREST {

	@POST
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Produces("text/plain")
	@Consumes("application/json")
	public String submit(RegistrationData data, @PathParam("id") Integer id, @Context UriInfo uriInfo) throws Exception {
		loadRace(id);
		RaceCategory raceCategory = loadRaceCategory(id, data.course, data.category);
		List<User> members = loadMembers(data.members);
		validate(raceCategory, members);

		Registration result = submit(data, raceCategory, members);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailDAO.getInstance().sendRegistrationCreation(result, members, baseUri);
		return result.getFormattedId();
	}

	private Registration submit(RegistrationData data, RaceCategory raceCategory, List<User> members) {
		Registration result = null;
		Registration registration = new Registration();
		registration.setTeamName(data.teamName);
		registration.setRaceCategory(raceCategory);
		registration.setSubmitter(UserDAO.getInstance().load(User.getLoggedIn().getEmail()));
		registration.setStatus(PENDENT);
		result = RegistrationDAO.getInstance().insert(registration);

		for (User member : members) {
			User atachedMember = UserDAO.getInstance().load(member.getId());
			// User atachedMember = member;
			TeamFormation teamFormation = new TeamFormation(registration, atachedMember);
			TeamFormationDAO.getInstance().insert(teamFormation);
		}

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
			exception.addViolation("members", "falta homem");
		}

		int female = count(members, FEMALE);
		if (category.getMinFemaleMembers() != null && female < category.getMinFemaleMembers()) {
			exception.addViolation("members", "falta mulher");
		}

		for (User member : members) {
			TeamFormation formation = TeamFormationDAO.getInstance().loadForRegistrationSubmissionValidation(
					raceCategory.getRace(), member);

			if (formation != null) {
				exception.addViolation("members", parse(member) + " já faz parte da equipe "
						+ formation.getRegistration().getTeamName());
			}

			if (member.getProfile().getPendencies() > 0 || member.getHealth().getPendencies() > 0) {
				exception.addViolation("members", parse(member) + " possui pendências cadastrais");
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}
	}

	private String parse(User user) {
		String result = null;

		if (user.equals(User.getLoggedIn())) {
			result = "você";
		} else {
			result = Strings.firstToUpper(user.getName().split(" +")[0].toLowerCase());
		}

		return result;
	}

	private int count(List<User> members, GenderType gender) {
		int result = 0;

		for (User user : members) {
			if (user.getProfile().getGender() == gender) {
				result++;
			}
		}

		return result;
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
