package adventure.rest;

import static java.util.Calendar.YEAR;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import adventure.entity.AnnualFee;
import adventure.entity.AnnualFeePayment;
import adventure.entity.Category;
import adventure.entity.City;
import adventure.entity.Course;
import adventure.entity.Period;
import adventure.entity.Race;
import adventure.entity.RaceStatusType;
import adventure.entity.User;
import adventure.persistence.AnnualFeeDAO;
import adventure.persistence.AnnualFeePaymentDAO;
import adventure.persistence.CityDAO;
import adventure.persistence.CourseDAO;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.UserDAO;
import adventure.rest.provider.PATCH;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Cache;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("race")
public class RaceREST {

	@GET
	@Path("{id}/form")
	@Produces("application/pdf")
	public byte[] resgistrationForm(@PathParam("id") Integer id) throws Exception {
		Race race = loadRaceDetails(id);

		Context context = new InitialContext();
		DataSource dataSource = (DataSource) context.lookup("java:jboss/datasources/PostgreSQLDS");
		Connection conn = dataSource.getConnection();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("RACE_ID", race.getId());

		String reportSource = Reflections.getResourceAsURL("/report/ficha_inscricao.jasper").getPath();
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportSource, params, conn);

		ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, oututStream);

		return oututStream.toByteArray();
	}

	@GET
	@Path("year/{year}")
	@Cache("max-age=28800")
	@Produces("application/json")
	public List<RaceData> year(@PathParam("year") Integer year) throws Exception {
		List<RaceData> result = new ArrayList<RaceData>();

		for (Race race : RaceDAO.getInstance().findByYear(year)) {
			RaceData data = new RaceData();
			data.id = race.getId();
			data.name = race.getName();
			data.date = race.getDate();
			data.status = race.getRaceStatusType();
			data.city = race.getCity().getName() != null ? race.getCity().getName() + "/"
					+ race.getCity().getState().getAbbreviation() : null;

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
		data.status = race.getRaceStatusType();
		data.registration = new RegistrationData();
		data.registration.prices.addAll(loadPeriod(race));
		data.registration.period = new PeriodData();
		data.registration.period.beginning = race.getRegistrationPeriod().getBeginning();
		data.registration.period.end = race.getRegistrationPeriod().getEnd();
		data.courses.addAll(loadCourse(race));
		data.organizers.addAll(loadOrganizer(race));

		return data;
	}

	@GET
	@Path("{id}/summary")
	@Cache("max-age=28800")
	@Produces("application/json")
	public RaceData loadSummary(@PathParam("id") Integer id) throws Exception {
		RaceData data = new RaceData();
		Race race = loadRaceDetails(id);

		data.id = race.getId();
		data.name = race.getName();
		data.date = race.getDate();
		data.description = race.getDescription();
		data.city = race.getCity().getName() != null ? race.getCity().getName() + "/"
				+ race.getCity().getState().getAbbreviation() : null;
		data.status = race.getRaceStatusType();

		return data;
	}

	@GET
	@Cache("max-age=604800000")
	@Path("{id}/banner/base64")
	@Produces("image/png")
	public byte[] getBannerBase64(@PathParam("id") Integer id) throws Exception {
		return getBannerBase64(id, null);
	}

	@GET
	@Cache("max-age=604800000")
	@Path("{id}/banner/base64/{width}")
	@Produces("image/png")
	public byte[] getBannerBase64(@PathParam("id") Integer id, @PathParam("width") Integer width) throws Exception {
		Race race = loadRace(id);
		return Base64.encodeBase64(resizeImage(race.getBanner(), 750, width));
	}

	@GET
	@Path("{id}/banner")
	@Cache("max-age=604800000")
	@Produces("image/png")
	public byte[] getBanner(@PathParam("id") Integer id) throws Exception {
		return getBanner(id, null);
	}

	@GET
	@Path("{id}/banner/{width}")
	@Cache("max-age=604800000")
	@Produces("image/png")
	public byte[] getBanner(@PathParam("id") Integer id, @PathParam("width") Integer width) throws Exception {
		Race race = loadRace(id);
		return resizeImage(race.getBanner(), 750, width);
	}

	private byte[] resizeImage(byte[] image, Integer defaultWidth, Integer width) throws Exception {
		byte[] result = image;

		if (width != null && width != defaultWidth) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Thumbnails.of(new ByteArrayInputStream(result)).scale((double) width / defaultWidth).toOutputStream(out);
			result = out.toByteArray();
		}

		return result;
	}

	@PUT
	@LoggedIn
	@Transactional
	@Path("{id}/banner")
	@Consumes("multipart/form-data")
	public void setBanner(@PathParam("id") Integer id, MultipartFormDataInput input) throws Exception {
		Race race = loadRace(id);
		checkPermission(race);

		InputPart file = input.getFormDataMap().get("file").get(0);
		InputStream inputStream = file.getBody(InputStream.class, null);
		race.setBanner(IOUtils.toByteArray(inputStream));

		RaceDAO.getInstance().update(race);
	}

	@GET
	@Path("{id}/logo")
	@Cache("max-age=604800000")
	@Produces("application/octet-stream")
	public byte[] getLogo(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);
		return race.getLogo();
	}

	@PUT
	@LoggedIn
	@Transactional
	@Path("{id}/logo")
	@Consumes("multipart/form-data")
	public void setLogo(@PathParam("id") Integer id, MultipartFormDataInput input) throws Exception {
		Race race = loadRace(id);
		checkPermission(race);

		InputPart file = input.getFormDataMap().get("file").get(0);
		InputStream inputStream = file.getBody(InputStream.class, null);
		race.setLogo(IOUtils.toByteArray(inputStream));

		RaceDAO.getInstance().update(race);
	}

	@PATCH
	@LoggedIn
	@Path("{id}")
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void update(@PathParam("id") Integer id, RaceUpdateData data) throws Exception {
		Race race = loadRace(id);
		checkPermission(race);

		if (data.name != null) {
			race.setName(data.name);
		}

		if (data.description != null) {
			race.setDescription(data.description);
		}

		if (data.date != null) {
			race.setDate(data.date);
		}

		if (data.cityId != null) {
			City city = CityDAO.getInstance().load(data.cityId);

			if (city == null) {
				throw new UnprocessableEntityException().addViolation("cityId", "Cidade inválida");
			}

			race.setCity(city);
		}

		RaceDAO.getInstance().update(race);
	}

	private List<PeriodData> loadPeriod(Race race) throws Exception {
		List<PeriodData> result = new ArrayList<PeriodData>();

		for (Period period : PeriodDAO.getInstance().find(race)) {
			PeriodData periodData = new PeriodData();
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

	private void checkPermission(Race race) throws ForbiddenException {
		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}
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
			Calendar calendar = Calendar.getInstance();
			Integer year = calendar.get(YEAR);
			AnnualFee annualFee = AnnualFeeDAO.getInstance().load(year);

			for (Integer userId : users) {
				User user = UserDAO.getInstance().loadBasics(userId);

				if (user == null) {
					throw new UnprocessableEntityException().addViolation("users", "usuário inválido");
				} else {

					OrderRowData row = new OrderRowData();
					row.id = user.getId();
					row.name = user.getProfile().getName();
					row.racePrice = period.getPrice().floatValue();

					if (race.getId().equals(2)) {
						row.annualFee = 0;
					} else {
						AnnualFeePayment annualFeePayment = AnnualFeePaymentDAO.getInstance().load(user, year);
						row.annualFee = annualFeePayment != null ? 0 : annualFee.getFee().floatValue();
					}

					row.amount = row.racePrice + row.annualFee;

					data.rows.add(row);
					data.total += row.amount;
				}
			}
		}

		period.setRace(null);

		return data;
	}

	private Race loadRace(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().load(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Race loadRaceDetails(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().loadForDetail(id);

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

	public static class RaceUpdateData {

		public String name;

		public String description;

		public Integer cityId;

		public Date date;

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

		public RaceStatusType status;
	}

	public static class RegistrationData {

		public PeriodData period;

		public List<PeriodData> prices = new ArrayList<PeriodData>();
	}

	public static class PeriodData {

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

		public float total;
	}

	public static class OrderRowData {

		public Integer id;

		public String name;

		public float racePrice;

		public float annualFee;

		public float amount;
	}
}
