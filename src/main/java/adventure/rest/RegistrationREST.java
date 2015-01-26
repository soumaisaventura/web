package adventure.rest;

import static java.util.Calendar.YEAR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.AnnualFee;
import adventure.entity.AnnualFeePayment;
import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.StatusType;
import adventure.entity.User;
import adventure.persistence.AnnualFeeDAO;
import adventure.persistence.AnnualFeePaymentDAO;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.UserDAO;
import adventure.rest.LocationREST.CityData;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.util.Beans;

@Path("registration")
public class RegistrationREST {
	
	public static RegistrationREST getInstance() {
		return Beans.getReference(RegistrationREST.class);
	}

	@GET
	@LoggedIn
	@Produces("application/json")
	public List<RegistrationData> find() throws Exception {
		List<RegistrationData> result = new ArrayList<RegistrationData>();
		User loggedInUser = User.getLoggedIn();

		for (Registration registration : RegistrationDAO.getInstance().find(loggedInUser)) {
			RegistrationData data = new RegistrationData();
			data.id = registration.getId();
			data.number = registration.getFormattedId();
			data.teamName = registration.getTeamName();
			data.status = registration.getStatus();
			data.race = new RaceData();
			data.race.id = registration.getRaceCategory().getRace().getId();
			data.race.name = registration.getRaceCategory().getRace().getName();
			data.race.date = registration.getRaceCategory().getRace().getDate();
			data.race.city = new CityData();
			data.race.city.id = registration.getRaceCategory().getRace().getCity().getId();
			data.race.city.name = registration.getRaceCategory().getRace().getCity().getName();
			data.race.city.state = registration.getRaceCategory().getRace().getCity().getState().getAbbreviation();

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	@GET
	@Path("{id}")
	@Produces("application/json")
	public RegistrationData load(@PathParam("id") Long id) throws Exception {
		Registration registration = loadRegistration(id);

		RegistrationData data = new RegistrationData();
		data.id = registration.getId();
		data.number = registration.getFormattedId();
		data.date = registration.getDate();
		data.status = registration.getStatus();
		data.submitter = new UserData();
		data.submitter.id = registration.getSubmitter().getId();
		data.submitter.email = registration.getSubmitter().getEmail();
		data.submitter.name = registration.getSubmitter().getProfile().getName();
		data.teamName = registration.getTeamName();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(registration.getDate());
		Integer year = calendar.get(YEAR);
		AnnualFee annualFee = AnnualFeeDAO.getInstance().load(year);

		List<User> list = UserDAO.getInstance().findTeamFormation(registration);
		User loggedInUser = User.getLoggedIn();

		if (!registration.getSubmitter().equals(loggedInUser) && !list.contains(loggedInUser)) {
			throw new ForbiddenException();
		}

		for (User user : list) {
			UserData member = new UserData();
			member.id = user.getId();
			member.email = user.getEmail();
			member.name = user.getProfile().getName();
			member.phone = user.getProfile().getMobile();

			AnnualFeePayment annualFeePayment = AnnualFeePaymentDAO.getInstance().load(user, year);
			member.bill = new BillData();
			member.bill.racePrice = registration.getPeriod().getPrice().floatValue();
			member.bill.annualFee = annualFeePayment != null ? 0 : annualFee.getFee().floatValue();
			member.bill.amount = member.bill.racePrice + member.bill.annualFee;
			data.teamFormation.add(member);
		}

		Race race = registration.getRaceCategory().getRace();
		data.race = new RaceData();
		data.race.id = race.getId();
		data.race.name = race.getName();
		data.race.date = race.getDate();
		data.race.city = new CityData();
		data.race.city.id = race.getCity().getId();
		data.race.city.name = race.getCity().getName();
		data.race.city.state = race.getCity().getState().getAbbreviation();

		for (User user : UserDAO.getInstance().findRaceOrganizers(race)) {
			UserData organizer = new UserData();
			organizer.id = user.getId();
			organizer.email = user.getEmail();
			organizer.name = user.getProfile().getName();
			organizer.phone = user.getProfile().getMobile();
			data.race.organizers.add(organizer);
		}

		data.course = new CourseData();
		data.course.id = registration.getRaceCategory().getCourse().getId();
		data.course.length = registration.getRaceCategory().getCourse().getLength();
		data.category = new CategoryData();
		data.category.id = registration.getRaceCategory().getCategory().getId();
		data.category.name = registration.getRaceCategory().getCategory().getName();

		return data;
	}

	private Registration loadRegistration(Long id) throws Exception {
		Registration result = RegistrationDAO.getInstance().loadForDetails(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	public static class RegistrationData {

		public Long id;

		public String number;

		public Date date;

		public StatusType status;

		public UserData submitter;

		public RaceData race;

		public CourseData course;

		public CategoryData category;

		public String teamName;

		public List<UserData> teamFormation = new ArrayList<UserData>();
	}

	public static class RaceData {

		public Integer id;

		public String name;

		public Date date;

		public CityData city;

		public List<UserData> organizers = new ArrayList<UserData>();
	}

	public static class CourseData {

		public Integer id;

		public Integer length;
	}

	public static class CategoryData {

		public Integer id;

		public String name;
	}

	public static class UserData {

		public Integer id;

		public String name;

		public String email;

		public String phone;

		public BillData bill;
	}

	public static class BillData {

		public float racePrice;

		public float annualFee;

		public float amount;
	}
}
