package adventure.rest;

import static adventure.entity.GenderType.FEMALE;
import static adventure.entity.GenderType.MALE;
import static adventure.entity.RegistrationStatusType.PENDENT;
import static adventure.util.Constants.NAME_SIZE;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;

import adventure.business.MailBusiness;
import adventure.entity.Category;
import adventure.entity.GenderType;
import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.entity.Registration;
import adventure.entity.RegistrationPeriod;
import adventure.entity.User;
import adventure.entity.UserRegistration;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceCategoryDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.UserDAO;
import adventure.persistence.UserRegistrationDAO;
import adventure.rest.data.RegistrationData;
import br.gov.frameworkdemoiselle.ForbiddenException;
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
	@Produces("text/plain")
	@Consumes("application/json")
	public String submit(RaceRegistrationData data, @PathParam("id") Integer id, @Context UriInfo uriInfo)
			throws Exception {
		loadRace(id);
		Date date = new Date();
		URI baseUri = uriInfo.getBaseUri().resolve("..");

		User submitter = UserDAO.getInstance().loadBasics(User.getLoggedIn().getEmail());
		RaceCategory raceCategory = loadRaceCategory(id, /* data.course, */data.category);
		List<User> members = loadMembers(data.members);
		RegistrationPeriod period = loadPeriod(raceCategory.getRace(), date);
		validate(raceCategory, members, submitter, data.teamName, baseUri);

		Registration result = submit(data, raceCategory, members, date, period, submitter);

		MailBusiness.getInstance().sendRegistrationCreation(result, baseUri);
		return result.getFormattedId();
	}

	private Registration submit(RaceRegistrationData data, RaceCategory raceCategory, List<User> members, Date date,
			RegistrationPeriod period, User submitter) {
		Registration result = null;
		Registration registration = new Registration();
		registration.setTeamName(data.teamName);
		registration.setRaceCategory(raceCategory);
		registration.setSubmitter(submitter);
		registration.setStatus(PENDENT);
		registration.setStatusDate(date);
		registration.setDate(date);
		registration.setPeriod(period);
		result = RegistrationDAO.getInstance().insert(registration);

		result.setUserRegistrations(new ArrayList<UserRegistration>());
		for (User member : members) {
			User atachedMember = UserDAO.getInstance().load(member.getId());
			UserRegistration teamFormation = new UserRegistration();
			teamFormation.setRegistration(registration);
			teamFormation.setUser(atachedMember);
			teamFormation.setRacePrice(period.getPrice());

			UserRegistrationDAO.getInstance().insert(teamFormation);
			result.getUserRegistrations().add(teamFormation);
		}

		return result;
	}

	@GET
	@LoggedIn
	@Path("list")
	@Transactional
	@Produces("application/json")
	public List<RegistrationData> find(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);
		List<RegistrationData> result = new ArrayList<RegistrationData>();

		// List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() /* && !organizers.contains(User.getLoggedIn()) */) {
			throw new ForbiddenException();
		}

		for (Registration registration : RegistrationDAO.getInstance().findToOrganizer(race)) {
			RegistrationData data = new RegistrationData();
			data.id = registration.getId();
			data.number = registration.getFormattedId();
			// data.teamName = registration.getTeamName();
			data.date = registration.getDate();
			data.status = registration.getStatus();

			// data.category = new CategoryData();
			// data.category.id = registration.getRaceCategory().getCategory().getId();
			// data.category.name = registration.getRaceCategory().getCategory().getName();

			// data.course = new CourseData();
			// data.course.id = registration.getRaceCategory().getCourse().getId();
			// data.course.name = registration.getRaceCategory().getCourse().getName();

			// data.teamFormation = new ArrayList<UserData>();
			// for (UserRegistration teamFormation : registration.getTeamFormations()) {
			// UserData userData = new UserData();
			// userData.id = teamFormation.getUser().getId();
			// userData.email = teamFormation.getUser().getEmail();
			// userData.name = teamFormation.getUser().getProfile().getName();
			// userData.phone = teamFormation.getUser().getProfile().getMobile();
			// userData.city = teamFormation.getUser().getProfile().getCity().getName();
			// userData.state = teamFormation.getUser().getProfile().getCity().getState().getAbbreviation();
			//
			// userData.bill = new BillData();
			// userData.bill.racePrice = teamFormation.getRacePrice().floatValue();
			//
			// data.teamFormation.add(userData);
			// }

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	private Race loadRace(Integer id) throws Exception {
		Race result = null;
//		Race result = RaceDAO.getInstance().loadForDetail(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private RaceCategory loadRaceCategory(Integer raceId, /* Integer courseId, */Integer categoryId) throws Exception {
		RaceCategory result = RaceCategoryDAO.getInstance().loadForRegistration(raceId, /* courseId, */categoryId);

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
				exception.addViolation("members", "Usuário " + id + " inválido.");
			} else if (result.contains(user)) {
				exception.addViolation("members", "Usuário " + id + " duplicado.");
			} else {
				result.add(user);
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}

		return result;
	}

	private RegistrationPeriod loadPeriod(Race race, Date date) throws Exception {
		RegistrationPeriod result = PeriodDAO.getInstance().load(race, date);

		if (result == null && User.getLoggedIn().getAdmin()) {
			List<RegistrationPeriod> periods = PeriodDAO.getInstance().find(race);
			result = periods != null && !periods.isEmpty() ? periods.get(periods.size() - 1) : null;
		}

		if (result == null) {
			throw new UnprocessableEntityException().addViolation("Fora do período de inscrição.");
		}

		return result;
	}

	private void validate(RaceCategory raceCategory, List<User> members, User submitter, String teamName, URI baseUri)
			throws Exception {
		int total = members.size();
		UnprocessableEntityException exception = new UnprocessableEntityException();
		Category category = raceCategory.getCategory();

		if (total > category.getTeamSize()) {
			exception.addViolation("members", "Tem muita gente na equipe.");
		} else if (total < category.getTeamSize()) {
			exception.addViolation("members", "A equipe está incompleta.");
		}

		int male = count(members, MALE);
		if (category.getMinMaleMembers() != null && male < category.getMinMaleMembers()) {
			exception.addViolation("members", "Falta atleta do sexo masculino.");
		}

		int female = count(members, FEMALE);
		if (category.getMinFemaleMembers() != null && female < category.getMinFemaleMembers()) {
			exception.addViolation("members", "Falta atleta do sexo feminino.");
		}

		for (User member : members) {
			UserRegistration formation = UserRegistrationDAO.getInstance().loadForRegistrationSubmissionValidation(
					raceCategory.getRace(), member);

			if (formation != null) {
				exception.addViolation("members", parse(member) + " já faz parte da equipe "
						+ formation.getRegistration().getTeamName() + ".");
			}

			if (member.getProfile().getPendencies() > 0 || member.getHealth().getPendencies() > 0) {
				exception.addViolation("members", parse(member) + " possui pendências cadastrais.");
				MailBusiness.getInstance().sendRegistrationFailed(member, submitter, raceCategory, teamName, baseUri);
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}
	}

	private String parse(User user) {
		String result = null;

		if (user.equals(User.getLoggedIn())) {
			result = "Você";
		} else {
			// result = Strings.firstToUpper(user.getName().split(" +")[0].toLowerCase());
			result = user.getName();
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

	public static class RaceRegistrationData {

		@NotEmpty
		@Size(max = NAME_SIZE)
		public String teamName;

		@NotNull
		public Integer category;

		// @NotNull
		// public Integer course;

		@NotEmpty
		public List<Integer> members;
	}
}
