package adventure.rest;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import adventure.business.ImageBusiness;
import adventure.business.RaceBusiness;
import adventure.entity.Category;
import adventure.entity.Championship;
import adventure.entity.Event;
import adventure.entity.Fee;
import adventure.entity.Modality;
import adventure.entity.Race;
import adventure.entity.RegistrationPeriod;
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
	public List<EventData> year(@PathParam("year") Integer year, @Context UriInfo uriInfo) throws Exception {
		List<EventData> result = new ArrayList<EventData>();

		for (Event event : EventDAO.getInstance().findByYear(year)) {
			EventData eventData = new EventData(uriInfo);

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
	@Cache("max-age=28800")
	@Produces("application/json")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}")
	public EventData load(@PathParam("slug") String slug, @QueryParam("summary") boolean summary,
			@Context UriInfo uriInfo) throws NotFoundException {
		RaceBusiness raceBusiness = RaceBusiness.getInstance();
		FeeDAO feeDAO = FeeDAO.getInstance();
		RaceDAO raceDAO = RaceDAO.getInstance();
		ChampionshipDAO championshipDAO = ChampionshipDAO.getInstance();
		CategoryDAO categoryDAO = CategoryDAO.getInstance();
		ModalityDAO modalityDAO = ModalityDAO.getInstance();
		PeriodDAO periodDAO = PeriodDAO.getInstance();

		EventData eventData = new EventData(uriInfo);
		Event event = loadEventDetails(slug);
		Date now = new Date();

		eventData.id = event.getSlug();
		eventData.internalId = event.getId();
		eventData.name = event.getName();
		eventData.description = event.getDescription();
		eventData.site = event.getSite();
		eventData.period = new PeriodData();
		eventData.period.beginning = event.getBeginning();
		eventData.period.end = event.getEnd();

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

		if (!summary) {
			// Races

			eventData.races = new ArrayList<RaceData>();
			for (Race race : raceDAO.findForEvent(event)) {
				List<Fee> championshipFees = new ArrayList<Fee>();

				RaceData raceData = new RaceData();
				raceData.id = race.getSlug();
				raceData.internalId = race.getId();
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
				raceData.period.beginning = race.getPeriod().getBeginning();
				raceData.period.end = race.getPeriod().getEnd();

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
				for (Category category : categoryDAO.find(race)) {
					CategoryData categoryData = new CategoryData();
					categoryData.name = category.getName();
					categoryData.description = category.getDescription();
					raceData.categories.add(categoryData);
				}

				// Registration Period + Prices

				List<RegistrationPeriod> periods = periodDAO.findForEvent(race);
				raceData.prices = new ArrayList<PeriodData>();
				for (RegistrationPeriod period : periodDAO.findForEvent(race)) {
					PeriodData periodData = new PeriodData();
					periodData.internalId = period.getId();
					periodData.beginning = period.getBeginning();
					periodData.end = period.getEnd();
					periodData.price = period.getPrice();
					raceData.prices.add(periodData);
				}

				// Current Period

				RegistrationPeriod currentPeriod = raceBusiness.getPeriod(now, periods);
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
		}

		// Organizers

		eventData.organizers = new ArrayList<UserData>();
		for (User organizer : UserDAO.getInstance().findOrganizers(event)) {
			UserData organizerData = new UserData(uriInfo);
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
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner")
	public byte[] getBanner(@PathParam("slug") String slug, @QueryParam("width") Integer width,
			@Context ServletContext context) throws Exception {
		Event event = loadEventBanner(slug);
		return ImageBusiness.getInstance().resize(loadBanner(event, context), 750, width);
	}

	private byte[] loadBanner(Event event, ServletContext context) throws Exception {
		byte[] result = event.getBanner();

		if (result == null) {
			InputStream in = context.getResourceAsStream("/images/banner_blank.png");
			result = IOUtils.toByteArray(in);
		}

		return result;
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

	private void checkPermission(Event event) throws ForbiddenException {
		List<User> organizers = UserDAO.getInstance().findOrganizers(event);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}
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
