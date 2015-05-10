package adventure.rest;

import static adventure.entity.GenderType.FEMALE;
import static adventure.entity.GenderType.MALE;
import static adventure.entity.RegistrationStatusType.PENDENT;
import static adventure.util.Constants.NAME_SIZE;
import static java.util.Calendar.YEAR;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
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

import adventure.entity.AnnualFee;
import adventure.entity.AnnualFeePayment;
import adventure.entity.Category;
import adventure.entity.GenderType;
import adventure.entity.Period;
import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.entity.Registration;
import adventure.entity.TeamFormation;
import adventure.entity.User;
import adventure.persistence.AnnualFeeDAO;
import adventure.persistence.AnnualFeePaymentDAO;
import adventure.persistence.MailDAO;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceCategoryDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.TeamFormationDAO;
import adventure.persistence.UserDAO;
import adventure.rest.RegistrationREST.BillData;
import adventure.rest.RegistrationREST.CategoryData;
import adventure.rest.RegistrationREST.CourseData;
import adventure.rest.RegistrationREST.RegistrationData;
import adventure.rest.RegistrationREST.UserData;
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

		RaceCategory raceCategory = loadRaceCategory(id, data.course, data.category);
		List<User> members = loadMembers(data.members);
		Period period = loadPeriod(raceCategory.getRace(), date);
		validate(raceCategory, members);

		Registration result = submit(data, raceCategory, members, date, period);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailDAO.getInstance().sendRegistrationCreation(result, baseUri);
		return result.getFormattedId();
	}

	private Registration submit(RaceRegistrationData data, RaceCategory raceCategory, List<User> members, Date date,
			Period period) {
		Registration result = null;
		Registration registration = new Registration();
		registration.setTeamName(data.teamName);
		registration.setRaceCategory(raceCategory);
		registration.setSubmitter(UserDAO.getInstance().load(User.getLoggedIn().getEmail()));
		registration.setStatus(PENDENT);
		registration.setStatusDate(date);
		registration.setDate(date);
		registration.setPeriod(period);
		result = RegistrationDAO.getInstance().insert(registration);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Integer year = calendar.get(YEAR);
		AnnualFee annualFee = AnnualFeeDAO.getInstance().load(year);

		result.setTeamFormations(new ArrayList<TeamFormation>());
		for (User member : members) {
			User atachedMember = UserDAO.getInstance().load(member.getId());
			TeamFormation teamFormation = new TeamFormation();
			teamFormation.setRegistration(registration);
			teamFormation.setUser(atachedMember);

			if (raceCategory.getCourse().getAnnualFee()) {
				AnnualFeePayment annualFeePayment = AnnualFeePaymentDAO.getInstance().load(member, year);
				teamFormation.setAnnualFee(annualFeePayment != null ? BigDecimal.valueOf(0) : annualFee.getFee());
			} else {
				teamFormation.setAnnualFee(BigDecimal.valueOf(0));
			}

			teamFormation.setRacePrice(period.getPrice());

			TeamFormationDAO.getInstance().insert(teamFormation);
			result.getTeamFormations().add(teamFormation);
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

		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}

		for (Registration registration : RegistrationDAO.getInstance().findToOrganizer(race)) {
			RegistrationData data = new RegistrationData();
			data.id = registration.getId();
			data.number = registration.getFormattedId();
			data.teamName = registration.getTeamName();
			data.date = registration.getDate();
			data.status = registration.getStatus();

			data.category = new CategoryData();
			data.category.id = registration.getRaceCategory().getCategory().getId();
			data.category.name = registration.getRaceCategory().getCategory().getName();

			data.course = new CourseData();
			data.course.id = registration.getRaceCategory().getCourse().getId();
			data.course.name = registration.getRaceCategory().getCourse().getName();

			data.teamFormation = new ArrayList<UserData>();
			for (TeamFormation teamFormation : registration.getTeamFormations()) {
				UserData userData = new UserData();
				userData.id = teamFormation.getUser().getId();
				userData.email = teamFormation.getUser().getEmail();
				userData.name = teamFormation.getUser().getProfile().getName();
				userData.phone = teamFormation.getUser().getProfile().getMobile();
				userData.city = teamFormation.getUser().getProfile().getCity().getName();
				userData.state = teamFormation.getUser().getProfile().getCity().getState().getAbbreviation();

				userData.bill = new BillData();
				userData.bill.racePrice = teamFormation.getRacePrice().floatValue();
				userData.bill.annualFee = teamFormation.getAnnualFee().floatValue();
				userData.bill.amount = userData.bill.racePrice + userData.bill.annualFee;

				data.teamFormation.add(userData);
			}

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	@GET
	@LoggedIn
	@Path("forms")
	@Transactional
	@Produces("application/json")
	public void registrationForms(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);

		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}

		// Connection connection = Beans.getReference(Connection.class);
		// Report rpt; // = new Report(parameterMap, connection);

		// Statement statement = null;
		// try {
		// connection = Database.getConnection();
		// statement = connection.createStatement();
		// HashMap parameterMap = new HashMap();
		// parameterMap.put("rtitle", "Report Title Here");//sending the report title as a parameter.
		// rpt.setReportName("productlist"); //productlist is the name of my jasper file.
		// rpt.callReport();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// try {
		// statement.close();
		// connection.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
	}

	private Race loadRace(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().loadForDetail(id);

		if (result == null) {
			throw new NotFoundException();
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
				exception.addViolation("members", "Usuário " + id + " inválido.");
			} else if (result.contains(user)) {
				exception.addViolation("members", "Usuário " + id + "duplicado.");
			} else {
				result.add(user);
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}

		return result;
	}

	private Period loadPeriod(Race race, Date date) throws Exception {
		Period result = PeriodDAO.getInstance().load(race, date);

		if (result == null) {
			throw new UnprocessableEntityException().addViolation("Fora do período de inscrição.");
		}

		return result;
	}

	private void validate(RaceCategory raceCategory, List<User> members) throws Exception {
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
			TeamFormation formation = TeamFormationDAO.getInstance().loadForRegistrationSubmissionValidation(
					raceCategory.getRace(), member);

			if (formation != null) {
				exception.addViolation("members", parse(member) + " já faz parte da equipe "
						+ formation.getRegistration().getTeamName() + ".");
			}

			if (member.getProfile().getPendencies() > 0 || member.getHealth().getPendencies() > 0) {
				exception.addViolation("members", parse(member) + " possui pendências cadastrais.");
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

		@NotNull
		public Integer course;

		@NotEmpty
		public List<Integer> members;
	}
}
