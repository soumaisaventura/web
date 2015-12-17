package adventure.rest;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import adventure.business.RaceBusiness;
import adventure.entity.Category;
import adventure.entity.Championship;
import adventure.entity.Event;
import adventure.entity.Fee;
import adventure.entity.Modality;
import adventure.entity.Period;
import adventure.entity.Race;
import adventure.entity.User;
import adventure.persistence.CategoryDAO;
import adventure.persistence.ChampionshipDAO;
import adventure.persistence.EventDAO;
import adventure.persistence.FeeDAO;
import adventure.persistence.ModalityDAO;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.UserDAO;
import adventure.rest.data.CategoryData;
import adventure.rest.data.ChampionshipData;
import adventure.rest.data.CityData;
import adventure.rest.data.CoordsData;
import adventure.rest.data.EventData;
import adventure.rest.data.LayoutData;
import adventure.rest.data.LocationData;
import adventure.rest.data.ModalityData;
import adventure.rest.data.PeriodData;
import adventure.rest.data.RaceData;
import adventure.rest.data.SportData;
import adventure.rest.data.UserData;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Cache;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("event")
public class EventREST {

	@GET
	@Path("year/{year}")
	@Cache("max-age=28800")
	@Produces("application/json")
	public List<EventData> year(@PathParam("year") Integer year) throws Exception {
		List<EventData> result = new ArrayList<EventData>();

		for (Event event : EventDAO.getInstance().findByYear(year)) {
			EventData eventData = new EventData();

			eventData.id = event.getSlug();
			eventData.name = event.getName();
			eventData.period = new PeriodData();
			eventData.period.beginning = event.getBeginning();
			eventData.period.end = event.getEnd();
			eventData.status = event.getStatus().getName();

			eventData.location = new LocationData();
			eventData.location.city = new CityData();
			eventData.location.city.name = event.getCity().getName();
			eventData.location.city.state = event.getCity().getState().getAbbreviation();

			result.add(eventData);
		}

		return result.isEmpty() ? null : result;
	}

	@GET
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/map")
	@Cache("max-age=28800")
	@Produces("application/json")
	public List<EventData> mapData(@PathParam("slug") String slug) throws Exception {
		List<EventData> result = new ArrayList<EventData>();

		for (Event event : EventDAO.getInstance().mapData()) {
			EventData data = new EventData();
			data.id = event.getSlug();
			data.name = event.getName();
			data.location = new LocationData();
			data.location.coords = new CoordsData();
			data.location.coords.latitude = event.getCoords().getLatitude();
			data.location.coords.longitude = event.getCoords().getLongitude();
			result.add(data);
		}

		return result;
	}

	@GET
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}")
	@Cache("max-age=28800")
	@Produces("application/json")
	public EventData load(@PathParam("slug") String slug) throws NotFoundException {
		RaceBusiness raceBusiness = RaceBusiness.getInstance();
		FeeDAO feeDAO = FeeDAO.getInstance();
		RaceDAO raceDAO = RaceDAO.getInstance();
		ChampionshipDAO championshipDAO = ChampionshipDAO.getInstance();
		CategoryDAO categoryDAO = CategoryDAO.getInstance();
		ModalityDAO modalityDAO = ModalityDAO.getInstance();
		PeriodDAO periodDAO = PeriodDAO.getInstance();

		EventData eventData = new EventData();
		Event event = loadEventDetails(slug);
		Date now = new Date();

		eventData.id = event.getSlug();
		eventData.name = event.getName();
		eventData.description = event.getDescription();
		eventData.site = event.getSite();
		eventData.period = new PeriodData();
		eventData.period.beginning = event.getBeginning();
		eventData.period.end = event.getEnd();

		// Layout

		if (!event.getLayout().isEmpty()) {
			eventData.layout = new LayoutData();
			eventData.layout.textColor = event.getLayout().getTextColor();
			eventData.layout.backgroundColor = event.getLayout().getBackgroundColor();
			eventData.layout.buttonColor = event.getLayout().getButtonColor();
		}

		// Location

		eventData.location = new LocationData();
		eventData.location.city = new CityData();
		eventData.location.city.name = event.getCity().getName();
		eventData.location.city.state = event.getCity().getState().getAbbreviation();

		if (!event.getCoords().isEmpty()) {
			eventData.location.coords = new CoordsData();
			eventData.location.coords.latitude = event.getCoords().getLatitude();
			eventData.location.coords.longitude = event.getCoords().getLongitude();
		}

		// Races

		eventData.races = new ArrayList<RaceData>();
		for (Race race : raceDAO.findForEvent(event)) {
			List<Fee> championshipFees = new ArrayList<Fee>();

			RaceData raceData = new RaceData();
			raceData.id = race.getSlug();
			raceData.name = race.getName();
			raceData.description = race.getDescription();
			raceData.distance = race.getDistance();
			raceData.status = race.getStatus().getName();

			// Sport

			raceData.sport = new SportData();
			raceData.sport.id = race.getSport().getAcronym();
			raceData.sport.name = race.getSport().getName();

			// Period

			raceData.period = new PeriodData();
			raceData.period.beginning = race.getBeginning();
			raceData.period.end = race.getEnd();

			raceData.championships = new ArrayList<ChampionshipData>();
			for (Championship championship : championshipDAO.findForEvent(race)) {
				ChampionshipData championshipData = new ChampionshipData();
				championshipData.id = championship.getSlug();
				championshipData.name = championship.getName();
				raceData.championships.add(championshipData);
				championshipFees.addAll(feeDAO.findForEvent(championship));
			}

			// Categories

			raceData.categories = new ArrayList<CategoryData>();
			for (Category category : categoryDAO.findForEvent(race)) {
				CategoryData categoryData = new CategoryData();
				categoryData.name = category.getName();
				categoryData.description = category.getDescription();
				raceData.categories.add(categoryData);
			}

			// Prices

			List<Period> periods = periodDAO.findForEvent(race);
			raceData.prices = new ArrayList<PeriodData>();
			for (Period period : periodDAO.findForEvent(race)) {
				PeriodData periodData = new PeriodData();
				periodData.beginning = period.getBeginning();
				periodData.end = period.getEnd();
				periodData.price = period.getPrice();
				raceData.prices.add(periodData);
			}

			// Current Period

			Period currentPeriod = raceBusiness.getPeriod(now, periods);
			if (currentPeriod != null) {
				raceData.currentPeriod = new PeriodData();
				raceData.currentPeriod.beginning = currentPeriod.getBeginning();
				raceData.currentPeriod.end = currentPeriod.getEnd();
				raceData.currentPeriod.price = currentPeriod.getPrice();
			}

			// Modalities

			raceData.modalities = new ArrayList<ModalityData>();
			for (Modality modality : modalityDAO.findForEvent(race)) {
				ModalityData modalityData = new ModalityData();
				modalityData.id = modality.getAcronym();
				modalityData.name = modality.getName();
				raceData.modalities.add(modalityData);
			}

			eventData.races.add(raceData);
		}

		// Countdown

		int days = Days.daysBetween(new LocalDate(now), new LocalDate(eventData.period.beginning)).getDays();
		eventData.period.countdown = days >= 0 ? days : -1;

		// Organizers

		eventData.organizers = new ArrayList<UserData>();
		for (User organizer : UserDAO.getInstance().findOrganizersForEvent(event)) {
			UserData organizerData = new UserData();
			organizerData.id = organizer.getId();
			organizerData.name = organizer.getProfile().getName();
			organizerData.email = organizer.getEmail();
			organizerData.mobile = organizer.getProfile().getMobile();
			eventData.organizers.add(organizerData);
		}

		return eventData;
	}

	@GET
	@Produces("image/png")
	@Cache("max-age=604800000")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner/base64")
	public byte[] getBannerBase64(@PathParam("slug") String slug) throws Exception {
		return getBannerBase64(slug, null);
	}

	@GET
	@Produces("image/png")
	@Cache("max-age=604800000")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner/base64/{width}")
	public byte[] getBannerBase64(@PathParam("slug") String slug, @PathParam("width") Integer width) throws Exception {
		Event event = loadEventBanner(slug);
		return Base64.encodeBase64(resizeImage(event.getBanner(), 750, width));
	}

	@GET
	@Produces("image/png")
	@Cache("max-age=604800000")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner")
	public byte[] getBanner(@PathParam("slug") String slug) throws Exception {
		return getBanner(slug, null);
	}

	@GET
	@Produces("image/png")
	@Cache("max-age=604800000")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner/{width}")
	public byte[] getBanner(@PathParam("slug") String slug, @PathParam("width") Integer width) throws Exception {
		Event event = loadEventBanner(slug);
		return resizeImage(event.getBanner(), 750, width);
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Consumes("multipart/form-data")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner")
	public void setBanner(@PathParam("slug") String slug, @NotEmpty MultipartFormDataInput input) throws Exception {
		Event event = loadEvent(slug);
		checkPermission(event);

		InputPart file = null;
		Map<String, List<InputPart>> formDataMap = input.getFormDataMap();

		for (Map.Entry<String, List<InputPart>> entry : formDataMap.entrySet()) {
			file = entry.getValue().get(0);
			break;
		}

		if (file == null) {
			throw new UnprocessableEntityException().addViolation("banner", "campo obrigatório");
		}

		InputStream inputStream = file.getBody(InputStream.class, null);
		event.setBanner(IOUtils.toByteArray(inputStream));

		EventDAO.getInstance().update(event);
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

	private void checkPermission(Event race) throws ForbiddenException {
		// List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		// if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
		// throw new ForbiddenException();
		// }
	}

	private Event loadEvent(String slug) throws NotFoundException {
		Event result = EventDAO.getInstance().load(slug);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Event loadEventDetails(String slug) throws NotFoundException {
		Event result = EventDAO.getInstance().loadForDetail(slug);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Event loadEventBanner(String slug) throws NotFoundException {
		Event result = EventDAO.getInstance().loadForBanner(slug);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}
}
