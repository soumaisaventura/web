package adventure.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.entity.Category;
import adventure.entity.Course;
import adventure.entity.Period;
import adventure.entity.Race;
import adventure.entity.User;
import adventure.persistence.CourseDAO;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Cache;

@Path("race")
public class RaceREST {

	@GET
	@Path("next")
	@Cache("max-age=28800")
	@Produces("application/json")
	public List<RaceData> next() throws Exception {
		List<RaceData> result = new ArrayList<RaceData>();

		for (Race race : RaceDAO.getInstance().findNext()) {
			RaceData data = new RaceData();
			data.id = race.getId();
			data.name = race.getName();
			data.date = race.getDate();
			data.registration = new RegistrationData();
			data.registration.open = race.getOpen();
			data.city = race.getCity().getName() != null ? race.getCity().getName() + "/"
					+ race.getCity().getState().getAbbreviation() : null;
			data.registration.periods = null;
			data.courses = null;
			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	@GET
	@Path("{id}")
	@Cache("max-age=28800")
	@Produces("application/json")
	public RaceData load(@PathParam("id") Integer id) throws Exception {
		RaceData data = new RaceData();
		Race race = loadRaceDetails(id);

		data.id = race.getId();
		data.name = race.getName();
		data.date = race.getDate();
		data.description = race.getDescription();
		data.city = race.getCity().getName() != null ? race.getCity().getName() + "/"
				+ race.getCity().getState().getAbbreviation() : null;
		data.registration = new RegistrationData();
		data.registration.open = race.getOpen();
		data.registration.periods.addAll(loadPeriod(race));
		data.courses.addAll(loadCourse(race));
		data.organizers.addAll(loadOrganizer(race));

		return data;
	}

	@GET
	@Path("{id}/summary")
	@Cache("max-age=28800")
	@Produces("application/json")
	public RaceData loadInfo(@PathParam("id") Integer id) throws Exception {
		RaceData data = new RaceData();
		Race race = loadRaceDetails(id);

		data.id = race.getId();
		data.name = race.getName();
		data.date = race.getDate();
		data.description = race.getDescription();
		data.registration = new RegistrationData();
		data.registration.open = race.getOpen();

		return data;
	}

	@GET
	@Path("{id}/banner")
	@Cache("max-age=28800")
	@Produces("application/octet-stream")
	public byte[] getBanner(@PathParam("id") Integer id) throws Exception {
		Race race = RaceDAO.getInstance().load(id);

		if (race == null) {
			throw new NotFoundException();
		}

		return race.getBanner();
	}

	private List<PeriodData> loadPeriod(Race race) throws Exception {
		List<PeriodData> result = new ArrayList<PeriodData>();

		for (Period period : PeriodDAO.getInstance().find(race)) {
			PeriodData periodData = new PeriodData();
			periodData.id = period.getId();
			periodData.beginning = period.getBeginning();
			periodData.end = period.getEnd();
			periodData.price = period.getPrice();
			result.add(periodData);
		}

		return result;
	}

	private List<CourseData> loadCourse(Race race) throws Exception {
		List<CourseData> result = new ArrayList<CourseData>();

		for (Course course : CourseDAO.getInstance().findWithCategories(race)) {
			CourseData courseData = new CourseData();
			courseData.id = course.getId();
			courseData.length = course.getLength();

			for (Category category : course.getCategories()) {
				CategoryData categoryData = new CategoryData();
				categoryData.id = category.getId();
				categoryData.name = category.getName();
				categoryData.description = category.getDescription();
				categoryData.teamSize = category.getTeamSize();
				courseData.categories.add(categoryData);
			}

			result.add(courseData);
		}

		return result;
	}

	private List<OrganizerData> loadOrganizer(Race race) throws Exception {
		List<OrganizerData> result = new ArrayList<OrganizerData>();

		for (User organizers : UserDAO.getInstance().findRaceOrganizers(race)) {
			OrganizerData organizerData = new OrganizerData();
			organizerData.id = organizers.getId();
			organizerData.email = organizers.getEmail();
			organizerData.name = organizers.getProfile().getName();

			result.add(organizerData);
		}

		return result;
	}

	@GET
	@Cache("max-age=28800")
	@Path("{id}/courses")
	@Produces("application/json")
	public List<CourseData> findCourses(@PathParam("id") Integer id) throws Exception {
		Race race = loadJustRaceId(id);
		List<CourseData> result = loadCourse(race);
		return result.isEmpty() ? null : result;
	}

	@GET
	@Path("{id}/order")
	@Produces("application/json")
	public OrderData getOrder(@PathParam("id") Integer id, @QueryParam("users") List<Integer> users) throws Exception {
		Race race = loadJustRaceId(id);

		Period period = PeriodDAO.getInstance().loadCurrent(race);
		OrderData data = new OrderData();

		if (users.isEmpty()) {
			throw new UnprocessableEntityException().addViolation("users", "parâmetro obrigatório");

		} else {
			for (Integer userId : users) {
				User user = UserDAO.getInstance().loadBasics(userId);

				if (user == null) {
					throw new UnprocessableEntityException().addViolation("users", "usuário inválido");
				} else {
					OrderRowData row = new OrderRowData();
					row.id = user.getId();
					row.name = user.getProfile().getName();
					row.racePrice = period.getPrice();
					row.annualFee = BigDecimal.valueOf(10);
					row.amount = row.racePrice.add(row.annualFee);

					data.rows.add(row);
					data.total = data.total.add(row.amount);
				}
			}
		}

		period.setRace(null);

		return data;
	}

	private Race loadRaceDetails(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().loadForDetails(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Race loadJustRaceId(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().loadJustId(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	public static class RaceData {

		public Integer id;

		public String name;

		public String description;

		public String city;

		public Date date;

		public List<CourseData> courses = new ArrayList<CourseData>();

		public List<OrganizerData> organizers = new ArrayList<OrganizerData>();

		public RegistrationData registration;
	}

	public static class RegistrationData {

		public boolean open;

		public List<PeriodData> periods = new ArrayList<PeriodData>();
	}

	public static class PeriodData {

		public Integer id;

		public Date beginning;

		public Date end;

		public BigDecimal price;
	}

	public static class CourseData {

		public Integer id;

		public Integer length;

		public List<CategoryData> categories = new ArrayList<CategoryData>();
	}

	public static class CategoryData {

		public Integer id;

		public String name;

		public String description;

		public Integer teamSize;
	}

	public static class OrganizerData {

		public Integer id;

		public String name;

		public String email;
	}

	public static class OrderData {

		public List<OrderRowData> rows = new ArrayList<OrderRowData>();

		public BigDecimal total = BigDecimal.valueOf(0);
	}

	public static class OrderRowData {

		public Integer id;

		public String name;

		public BigDecimal racePrice;

		public BigDecimal annualFee;

		public BigDecimal amount = BigDecimal.valueOf(0);
	}
}
