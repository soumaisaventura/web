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

import adventure.business.FeeBusiness;
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
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("event")
public class EventREST {

	@GET
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/map")
	// @Cache("max-age=28800")
	@Produces("application/json")
	public List<EventData> mapData(@PathParam("slug") String slug) throws Exception {
		RaceBusiness raceBusiness = RaceBusiness.getInstance();
		PeriodDAO periodDAO = PeriodDAO.getInstance();
		Date now = new Date();

		List<EventData> result = new ArrayList<EventData>();

		for (Event event : EventDAO.getInstance().mapData()) {
			EventData data = new EventData();
			data.id = event.getSlug();
			data.name = event.getName();
			data.races = new ArrayList<RaceData>();

			for (Race race : event.getRaces()) {
				List<Period> periods = periodDAO.findForEvent(race);

				RaceData raceData = new RaceData();
				raceData.id = race.getSlug();
				raceData.name = race.getName2();
				raceData.location = new LocationData();
				raceData.location.coords = new CoordsData();
				raceData.location.coords.latitude = race.getCoords().getLatitude();
				raceData.location.coords.longitude = race.getCoords().getLongitude();
				raceData.status = raceBusiness.getStatus(race, now, periods);
				data.races.add(raceData);
			}

			if (data.races.isEmpty()) {
				data.races = null;
			}

			result.add(data);
		}

		return result;
	}

	@GET
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}")
	// @Cache("max-age=28800")
	@Produces("application/json")
	public EventData load(@PathParam("slug") String slug) throws NotFoundException {
		FeeBusiness feeBusiness = FeeBusiness.getInstance();
		RaceBusiness raceBusiness = RaceBusiness.getInstance();
		FeeDAO feeDAO = FeeDAO.getInstance();
		RaceDAO raceDAO = RaceDAO.getInstance();
		ChampionshipDAO championshipDAO = ChampionshipDAO.getInstance();
		CategoryDAO categoryDAO = CategoryDAO.getInstance();
		ModalityDAO modalityDAO = ModalityDAO.getInstance();
		PeriodDAO periodDAO = PeriodDAO.getInstance();

		EventData data = new EventData();
		Event event = loadEventDetails(slug);
		Date now = new Date();

		data.id = event.getSlug();
		data.name = event.getName();
		data.description = event.getDescription();
		data.site = event.getSite();

		data.layout = new LayoutData();
		data.layout.textColor = event.getLayout().getTextColor();
		data.layout.backgroundColor = event.getLayout().getBackgroundColor();
		data.layout.buttonColor = event.getLayout().getButtonColor();

		// Races

		data.races = new ArrayList<RaceData>();
		for (Race race : raceDAO.findForEvent(event)) {
			List<Fee> championshipFees = new ArrayList<Fee>();
			List<Fee> raceFees = feeDAO.findForEvent(race);

			RaceData raceData = new RaceData();
			raceData.id = race.getSlug();
			raceData.name = race.getName();
			raceData.description = race.getDescription();

			// Race Sport

			raceData.sport = new SportData();
			raceData.sport.id = race.getSport().getAcronym();
			raceData.sport.name = race.getSport().getName();

			// Race Period

			raceData.period = new PeriodData();
			raceData.period.beginning = race.getBeginning();
			raceData.period.end = race.getEnd();

			// Race Location

			raceData.location = new LocationData();
			raceData.location.city = new CityData();
			raceData.location.city.name = race.getCity().getName();
			raceData.location.city.state = race.getCity().getState().getAbbreviation();
			raceData.location.coords = new CoordsData();
			raceData.location.coords.latitude = race.getCoords().getLatitude();
			raceData.location.coords.longitude = race.getCoords().getLongitude();

			// Race Championships

			raceData.championships = new ArrayList<ChampionshipData>();
			for (Championship championship : championshipDAO.findForEvent(race)) {
				ChampionshipData championshipData = new ChampionshipData();
				championshipData.id = championship.getSlug();
				championshipData.name = championship.getName();
				raceData.championships.add(championshipData);
				championshipFees.addAll(feeDAO.findForEvent(championship));
			}

			// Race Categories

			raceData.categories = new ArrayList<CategoryData>();
			for (Category category : categoryDAO.findForEvent(race)) {
				CategoryData categoryData = new CategoryData();
				categoryData.name = category.getName();
				categoryData.description = category.getDescription();
				raceData.categories.add(categoryData);
			}

			// Race Prices
			List<Period> periods = periodDAO.findForEvent(race);
			raceData.prices = new ArrayList<PeriodData>();
			for (Period period : periodDAO.findForEvent(race)) {
				PeriodData periodData = new PeriodData();
				periodData.beginning = period.getBeginning();
				periodData.end = period.getEnd();
				periodData.price = feeBusiness.applyForEvent(period.getPrice(), raceFees, championshipFees);
				raceData.prices.add(periodData);

				// if (now.after(periodData.beginning) && now.before(periodData.end)) {
				// raceData.currentPrice = periodData.price;
				// raceData.status = RaceStatusData.OPEN;
				// }
			}

			// Race Price & Race Status
			raceData.status = raceBusiness.getStatus(race, now, periods);
			raceData.currentPrice = raceBusiness.getCurrentPrice(race, now, periods);

			// if (raceData.currentPrice == null && !raceData.prices.isEmpty()) {
			// if (now.before(raceData.prices.get(0).beginning)) {
			// raceData.currentPrice = raceData.prices.get(0).price;
			// raceData.status = RaceStatusData.SOON;
			// } else {
			// raceData.currentPrice = raceData.prices.get(raceData.prices.size() - 1).price;
			//
			// if (now.before(raceData.period.beginning)) {
			// raceData.status = RaceStatusData.CLOSED;
			// } else {
			// raceData.status = RaceStatusData.END;
			// }
			// }
			// }

			// Modalities

			raceData.modalities = new ArrayList<ModalityData>();
			for (Modality modality : modalityDAO.findForEvent(race)) {
				ModalityData modalityData = new ModalityData();
				modalityData.id = modality.getAcronym();
				modalityData.name = modality.getName();
				raceData.modalities.add(modalityData);
			}

			data.races.add(raceData);
		}

		// Organizers

		data.organizers = new ArrayList<UserData>();
		for (User organizer : UserDAO.getInstance().findOrganizersForEvent(event)) {
			UserData organizerData = new UserData();
			// organizerData.id = organizer.getId();
			organizerData.name = organizer.getProfile().getName();
			organizerData.email = organizer.getEmail();
			organizerData.mobile = organizer.getProfile().getMobile();
			data.organizers.add(organizerData);
		}

		return data;
	}

	@GET
	@Produces("image/png")
	// @Cache("max-age=604800000")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner/base64")
	public byte[] getBannerBase64(@PathParam("slug") String slug) throws Exception {
		return getBannerBase64(slug, null);
	}

	@GET
	@Produces("image/png")
	// @Cache("max-age=604800000")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner/base64/{width}")
	public byte[] getBannerBase64(@PathParam("slug") String slug, @PathParam("width") Integer width) throws Exception {
		Event event = loadEventBanner(slug);
		return Base64.encodeBase64(resizeImage(event.getBanner(), 750, width));
	}

	@GET
	@Produces("image/png")
	// @Cache("max-age=604800000")
	@Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner")
	public byte[] getBanner(@PathParam("slug") String slug) throws Exception {
		return getBanner(slug, null);
	}

	@GET
	@Produces("image/png")
	// @Cache("max-age=604800000")
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
