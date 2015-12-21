package adventure.rest;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;
import static adventure.util.Constants.RACE_SLUG_PATTERN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import adventure.entity.Category;
import adventure.entity.Race;
import adventure.entity.RegistrationPeriod;
import adventure.entity.User;
import adventure.persistence.CategoryDAO;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.UserDAO;
import adventure.rest.data.CategoryData;
import adventure.rest.data.CityData;
import adventure.rest.data.EventData;
import adventure.rest.data.LocationData;
import adventure.rest.data.PeriodData;
import adventure.rest.data.RaceData;
import adventure.rest.data.UserData;
import adventure.rest.provider.PATCH;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Cache;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("event")
public class RaceREST {

	// OLD

	// @GET
	// @Path("year/{year}")
	// @Cache("max-age=28800")
	// @Produces("application/json")
	// public List<RaceData> year(@PathParam("year") Integer year) throws Exception {
	// List<RaceData> result = new ArrayList<RaceData>();
	//
	// // for (Race race : RaceDAO.getInstance().findByYear(year)) {
	// // RaceData data = new RaceData();
	// // data.id = race.getId();
	// // data.name = race.getName();
	// // // data.date = race.getDate();
	// // data.status = race.getStatus().getName();
	// // // data.city = race.getCity().getName() != null ? race.getCity().getName() + "/"
	// // // + race.getCity().getState().getAbbreviation() : null;
	// //
	// // // data.courses = null;
	// // result.add(data);
	// // }
	//
	// return result.isEmpty() ? null : result;
	// }

	@GET
	@Path("{id}")
	@Cache("max-age=28800")
	@Produces("application/json")
	public RaceData load(@PathParam("id") Integer id) throws Exception {
		RaceData data = new RaceData();
		// Race race = loadRaceDetails(id);
		//
		// data.id = race.getId();
		// data.name = race.getName();
		// // data.date = race.getDate();
		// // data.site = race.getSite();
		// data.description = race.getDescription();
		// // data.city = race.getCity().getName() != null ? race.getCity().getName() + "/"
		// // + race.getCity().getState().getAbbreviation() : null;
		// data.status = race.getStatus().getName();
		// data.registration = new RegistrationData();
		// data.registration.prices.addAll(loadPeriod(race));
		// data.registration.period = new PeriodData();
		// data.registration.period.beginning = race.getRegistrationPeriod().getBeginning();
		// data.registration.period.end = race.getRegistrationPeriod().getEnd();
		// // data.courses.addAll(loadCourse(race));
		// // data.organizers.addAll(loadOrganizer(race));

		return data;
	}

	@GET
	@Cache("max-age=28800")
	@Produces("application/json")
	@Path("{eventSlug: " + EVENT_SLUG_PATTERN + "}/{raceSlug: " + RACE_SLUG_PATTERN + "}/summary")
	public RaceData loadSummary(@PathParam("raceSlug") String raceSlug, @PathParam("eventSlug") String eventSlug)
			throws Exception {
		RaceData data = new RaceData();
		Race race = loadRaceDetails(raceSlug, eventSlug);

		data.id = race.getSlug();
		data.internalId = race.getId();
		data.name = race.getName();
		data.description = race.getDescription();
		data.status = race.getStatus().getName();

		data.period = new PeriodData();
		data.period.beginning = race.getPeriod().getBeginning();
		data.period.end = race.getPeriod().getEnd();

		data.event = new EventData();
		data.event.id = race.getEvent().getSlug();
		data.event.internalId = race.getEvent().getId();
		data.event.name = race.getEvent().getName();
		data.event.site = race.getEvent().getSite();

		data.event.location = new LocationData();
		data.event.location.city = new CityData();
		data.event.location.city.id = race.getEvent().getCity().getId();
		data.event.location.city.name = race.getEvent().getCity().getName();
		data.event.location.city.state = race.getEvent().getCity().getState().getAbbreviation();

		return data;
	}

	// @GET
	// @Cache("max-age=604800000")
	// @Path("{id}/banner/base64")
	// @Produces("image/png")
	// public byte[] getBannerBase64(@PathParam("id") Integer id) throws Exception {
	// return getBannerBase64(id, null);
	// }

	// @GET
	// @Cache("max-age=604800000")
	// @Path("{id}/banner/base64/{width}")
	// @Produces("image/png")
	// public byte[] getBannerBase64(@PathParam("id") Integer id, @PathParam("width") Integer width) throws Exception {
	// Race race = loadRace(id);
	// return Base64.encodeBase64(resizeImage(race.getBanner(), 750, width));
	// }

	// @GET
	// @Path("{id}/banner")
	// @Cache("max-age=604800000")
	// @Produces("image/png")
	// public byte[] getBanner(@PathParam("id") Integer id) throws Exception {
	// return getBanner(id, null);
	// }

	// @GET
	// @Path("{id}/banner/{width}")
	// @Cache("max-age=604800000")
	// @Produces("image/png")
	// public byte[] getBanner(@PathParam("id") Integer id, @PathParam("width") Integer width) throws Exception {
	// Race race = loadRace(id);
	// return resizeImage(race.getBanner(), 750, width);
	// }

	// private byte[] resizeImage(byte[] image, Integer defaultWidth, Integer width) throws Exception {
	// byte[] result = image;
	//
	// if (width != null && width != defaultWidth) {
	// ByteArrayOutputStream out = new ByteArrayOutputStream();
	// Thumbnails.of(new ByteArrayInputStream(result)).scale((double) width / defaultWidth).toOutputStream(out);
	// result = out.toByteArray();
	// }
	//
	// return result;
	// }

	// @PUT
	// @LoggedIn
	// @Transactional
	// @Path("{id}/banner")
	// @Consumes("multipart/form-data")
	// public void setBanner(@PathParam("id") Integer id, MultipartFormDataInput input) throws Exception {
	// Race race = loadRace(id);
	// checkPermission(race);
	//
	// InputPart file = input.getFormDataMap().get("file").get(0);
	// InputStream inputStream = file.getBody(InputStream.class, null);
	// race.setBanner(IOUtils.toByteArray(inputStream));
	//
	// RaceDAO.getInstance().update(race);
	// }

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

		// if (data.date != null) {
		// race.setDate(data.date);
		// }

		// if (data.cityId != null) {
		// City city = CityDAO.getInstance().load(data.cityId);
		//
		// if (city == null) {
		// throw new UnprocessableEntityException().addViolation("cityId", "Cidade inválida");
		// }
		//
		// race.setCity(city);
		// }

		RaceDAO.getInstance().update(race);
	}

	private List<PeriodData> loadPeriod(Race race) throws Exception {
		List<PeriodData> result = new ArrayList<PeriodData>();

		for (RegistrationPeriod period : PeriodDAO.getInstance().find(race)) {
			PeriodData periodData = new PeriodData();
			periodData.beginning = period.getBeginning();
			periodData.end = period.getEnd();
			periodData.price = period.getPrice();
			result.add(periodData);
		}

		return result;
	}

	// private List<OrganizerData> loadOrganizer(Race race) throws Exception {
	// List<OrganizerData> result = new ArrayList<OrganizerData>();
	//
	// for (User organizers : UserDAO.getInstance().findRaceOrganizers(race)) {
	// OrganizerData organizerData = new OrganizerData();
	// organizerData.id = organizers.getId();
	// organizerData.email = organizers.getEmail();
	// organizerData.name = organizers.getProfile().getName();
	//
	// result.add(organizerData);
	// }
	//
	// return result;
	// }

	private void checkPermission(Race race) throws ForbiddenException {
		// List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() /* && !organizers.contains(User.getLoggedIn()) */) {
			throw new ForbiddenException();
		}
	}

	@GET
	@Cache("max-age=28800")
	@Produces("application/json")
	@Path("{eventSlug: " + EVENT_SLUG_PATTERN + "}/{raceSlug: " + RACE_SLUG_PATTERN + "}/categories")
	public List<CategoryData> findCategories(@PathParam("raceSlug") String raceSlug,
			@PathParam("eventSlug") String eventSlug) throws Exception {
		Race race = loadRaceDetails(raceSlug, eventSlug);
		List<CategoryData> result = loadCourse(race);
		return result.isEmpty() ? null : result;
	}

	private List<CategoryData> loadCourse(Race race) throws Exception {
		List<CategoryData> result = new ArrayList<CategoryData>();

		for (Category category : CategoryDAO.getInstance().find(race)) {
			CategoryData categoryData = new CategoryData();
			categoryData.id = category.getId();
			categoryData.name = category.getName();
			categoryData.description = category.getDescription();
			categoryData.teamSize = category.getTeamSize();
			categoryData.minMaleMembers = category.getMinMaleMembers();
			categoryData.minFemaleMembers = category.getMinFemaleMembers();
			result.add(categoryData);
		}

		return result;
	}

	@GET
	@Cache("max-age=28800")
	@Produces("application/json")
	@Path("{eventSlug: " + EVENT_SLUG_PATTERN + "}/{raceSlug: " + RACE_SLUG_PATTERN + "}/order")
	public List<UserData> getOrder(@PathParam("raceSlug") String raceSlug, @PathParam("eventSlug") String eventSlug,
			@QueryParam("users_ids") List<Integer> users) throws Exception {
		Race race = loadRaceDetails(raceSlug, eventSlug);

		RegistrationPeriod period = PeriodDAO.getInstance().loadCurrent(race);
		List<UserData> result = new ArrayList<UserData>();

		if (users.isEmpty()) {
			throw new UnprocessableEntityException().addViolation("users", "parâmetro obrigatório");

		} else {
			for (Integer userId : users) {
				User user = UserDAO.getInstance().loadBasics(userId);

				if (user == null) {
					throw new UnprocessableEntityException().addViolation("users", "usuário inválido");
				} else {
					UserData row = new UserData();
					row.id = user.getId();
					row.name = user.getProfile().getName();
					row.racePrice = period.getPrice();

					result.add(row);
				}
			}
		}

		period.setRace(null);
		return result;
	}

	private Race loadRace(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().load(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Race loadRaceDetails(String raceSlug, String eventSlug) throws Exception {
		Race result = RaceDAO.getInstance().loadForDetail(raceSlug, eventSlug);

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

	// public static class RaceData {
	//
	// public Integer id;
	//
	// public String name;
	//
	// public String description;
	//
	// // public String city;
	//
	// // public Date date;
	//
	// // public String site;
	//
	// // public List<CourseData> courses = new ArrayList<CourseData>();
	//
	// // public List<OrganizerData> organizers = new ArrayList<OrganizerData>();
	//
	// public RegistrationData registration;
	//
	// public String status;
	// }

	public static class RegistrationData {

		public PeriodData period;

		public List<PeriodData> prices = new ArrayList<PeriodData>();
	}

	// public static class PeriodData {
	//
	// public Date beginning;
	//
	// public Date end;
	//
	// public BigDecimal price;
	// }

	// public static class CourseData {
	//
	// public Integer id;
	//
	// public String name;
	//
	// public List<CategoryData> categories = new ArrayList<CategoryData>();
	// }

	// public static class CategoryData {
	//
	// public Integer id;
	//
	// public String name;
	//
	// public String description;
	//
	// public Integer teamSize;
	// }

	// public static class OrganizerData {
	//
	// public Integer id;
	//
	// public String name;
	//
	// public String email;
	// }

	public static class OrderRowData {

		public Integer id;

		public String name;

		public float racePrice;
	}
}
