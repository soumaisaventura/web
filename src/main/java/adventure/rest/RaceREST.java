package adventure.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.entity.Account;
import adventure.entity.Category;
import adventure.entity.Course;
import adventure.entity.Period;
import adventure.entity.Race;
import adventure.persistence.AccountDAO;
import adventure.persistence.CourseDAO;
import adventure.persistence.PeriodDAO;
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
	private RaceDAO raceDAO;

	@GET
	@Path("next")
	@Produces("application/json")
	public List<RaceData> next() throws Exception {
		List<RaceData> result = new ArrayList<RaceREST.RaceData>();

		for (Race race : raceDAO.findNext()) {
			RaceData data = new RaceData();
			data.id = race.getId();
			data.name = race.getName();
			data.date = race.getDate();
			data.register = new RegisterData();
			data.register.open = race.getOpen();
			data.register.periods = null;
			data.courses = null;
			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	// @GET
	// @Path("calendar")
	// @Produces("text/calendar")
	// public Response generateCalendar() {
	// Calendar calendar = new Calendar();
	// // Generate your calendar here
	// ResponseBuilder builder = Response.ok();
	// builder.header("content-disposition", "attachment;filename=calendar.ics");
	// builder.entity(calendar.toString());
	// return builder.build();
	// }

	@GET
	@Path("{id}")
	@Produces("application/json")
	public RaceData load(@PathParam("id") Long id) throws Exception {
		RaceData data = new RaceData();
		Race race = loadRaceDetails(id);

		data.id = race.getId();
		data.name = race.getName();
		data.date = race.getDate();
		data.register = new RegisterData();
		data.register.open = race.getOpen();

		for (Period period : Beans.getReference(PeriodDAO.class).find(race)) {
			PeriodData periodData = new PeriodData();
			periodData.id = period.getId();
			periodData.beginning = period.getBeginning();
			periodData.end = period.getEnd();
			periodData.price = period.getPrice();
			data.register.periods.add(periodData);
		}

		data.courses.addAll(loadCourse(race));
		return data;
	}

	private List<CourseData> loadCourse(Race race) throws Exception {
		List<CourseData> result = new ArrayList<RaceREST.CourseData>();

		for (Course course : Beans.getReference(CourseDAO.class).findWithCategories(race)) {
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

	@GET
	@Path("{id}/users")
	@Produces("application/json")
	public List<User> search(@PathParam("id") Long id, @QueryParam("q") String q,
			@QueryParam("excludes") List<Long> excludes) throws Exception {
		Race race = loadJustRaceId(id);
		validate(q);
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
	@Path("{id}/courses")
	@Produces("application/json")
	public List<CourseData> findCourses(@PathParam("id") Long id) throws Exception {
		Race race = loadJustRaceId(id);
		List<CourseData> result = loadCourse(race);
		return result.isEmpty() ? null : result;
	}

	@GET
	@Path("{id}/order")
	@Produces("application/json")
	public OrderData getOrder(@PathParam("id") Long id, @QueryParam("users") List<Long> users) throws Exception {
		Race race = loadJustRaceId(id);

		Period period = Beans.getReference(PeriodDAO.class).loadCurrent(race);
		OrderData data = new OrderData();

		if (users.isEmpty()) {
			throw new UnprocessableEntityException().addViolation("users", "parâmetro obrigatório");

		} else {
			for (Long accountId : users) {
				Account account = Beans.getReference(AccountDAO.class).loadForBill(accountId);

				if (account == null) {
					throw new UnprocessableEntityException().addViolation("users", "usuário inválido");
				} else {
					OrderRowData row = new OrderRowData();
					row.id = account.getId();
					row.name = account.getProfile().getName();
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

	private Race loadRaceDetails(Long id) throws Exception {
		Race result = raceDAO.loadForDetails(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Race loadJustRaceId(Long id) throws Exception {
		Race result = raceDAO.loadJustId(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	public static class RaceData {

		public Long id;

		public String name;

		public Date date;

		public List<CourseData> courses = new ArrayList<CourseData>();

		public RegisterData register;
	}

	public static class RegisterData {

		public boolean open;

		public List<PeriodData> periods = new ArrayList<PeriodData>();
	}

	public static class PeriodData {

		public Long id;

		public Date beginning;

		public Date end;

		public BigDecimal price;
	}

	public static class CourseData {

		public Long id;

		public Integer length;

		public List<CategoryData> categories = new ArrayList<CategoryData>();
	}

	public static class CategoryData {

		public Long id;

		public String name;

		public String description;

		public Integer teamSize;

	}

	public static class CategoryData2 {

		public Long id;

		public String name;

		public String description;

		public Integer teamSize;

		public Long course;
	}

	public static class OrderData {

		public List<OrderRowData> rows = new ArrayList<OrderRowData>();

		public BigDecimal total = BigDecimal.valueOf(0);
	}

	public static class OrderRowData {

		public Long id;

		public String name;

		public BigDecimal racePrice;

		public BigDecimal annualFee;

		public BigDecimal amount = BigDecimal.valueOf(0);
	}
}
