package adventure.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.codec.binary.Base64;

import adventure.business.FeeBusiness;
import adventure.entity.Championship;
import adventure.entity.Event;
import adventure.entity.Fee;
import adventure.entity.Modality;
import adventure.entity.Period;
import adventure.entity.Race;
import adventure.entity.User;
import adventure.persistence.ChampionshipDAO;
import adventure.persistence.EventDAO;
import adventure.persistence.FeeDAO;
import adventure.persistence.ModalityDAO;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RaceDAO;
import adventure.rest.data.ChampionshipData;
import adventure.rest.data.CityData;
import adventure.rest.data.EventData;
import adventure.rest.data.LayoutData;
import adventure.rest.data.ModalityData;
import adventure.rest.data.PeriodData;
import adventure.rest.data.RaceData;
import adventure.rest.data.SportData;
import adventure.rest.data.StateData;
import adventure.rest.data.UserData;
import br.gov.frameworkdemoiselle.NotFoundException;

@Path("event")
public class EventREST {

	@GET
	@Path("{slug: [\\w\\d_\\-/]+}")
	// @Cache("max-age=28800")
	@Produces("application/json")
	public EventData load(@PathParam("slug") String slug) throws Exception {
		FeeBusiness feeBusiness = FeeBusiness.getInstance();
		EventData data = new EventData();
		Event event = loadEventDetails(slug);

		data.id = event.getSlug();
		data.name = event.getName();
		data.description = event.getDescription();
		data.site = event.getSite();

		data.layout = new LayoutData();
		data.layout.textColor = event.getLayout().getTextColor();
		data.layout.backgroundColor = event.getLayout().getBackgroundColor();
		data.layout.buttonColor = event.getLayout().getButtonColor();

		data.races = new ArrayList<RaceData>();
		for (Race race : RaceDAO.getInstance().findForEvent(event)) {
			List<Fee> championshipFees = new ArrayList<Fee>();
			List<Fee> raceFees = FeeDAO.getInstance().findForEvent(race);

			RaceData raceData = new RaceData();
			raceData.id = race.getSlug();
			raceData.name = race.getName();
			raceData.description = race.getDescription();

			raceData.sport = new SportData();
			raceData.sport.id = race.getSport().getAcronym();
			raceData.sport.name = race.getSport().getName();

			raceData.period = new PeriodData();
			raceData.period.beginning = race.getBeginning();
			raceData.period.end = race.getEnd();

			raceData.city = new CityData();
			raceData.city.name = race.getCity().getName();
			raceData.city.state = new StateData();
			raceData.city.state.id = race.getCity().getState().getAbbreviation();
			raceData.city.state.name = race.getCity().getState().getName();

			raceData.championships = new ArrayList<ChampionshipData>();
			for (Championship championship : ChampionshipDAO.getInstance().findForEvent(race)) {
				ChampionshipData championshipData = new ChampionshipData();
				championshipData.id = championship.getSlug();
				championshipData.name = championship.getName();
				raceData.championships.add(championshipData);
				championshipFees.addAll(FeeDAO.getInstance().findForEvent(championship));
			}

			raceData.prices = new ArrayList<PeriodData>();
			for (Period period : PeriodDAO.getInstance().findForEvent(race)) {
				PeriodData periodData = new PeriodData();
				periodData.beginning = period.getBeginning();
				periodData.end = period.getEnd();
				periodData.price = feeBusiness.applyForEvent(period.getPrice(), raceFees, championshipFees);
				raceData.prices.add(periodData);
			}

			raceData.modalities = new ArrayList<ModalityData>();
			for (Modality modality : ModalityDAO.getInstance().findForEvent(race)) {
				ModalityData modalityData = new ModalityData();
				modalityData.id = modality.getAcronym();
				modalityData.name = modality.getName();
				raceData.modalities.add(modalityData);
			}

			data.races.add(raceData);
		}

		data.organizers = new ArrayList<UserData>();
		for (User organizer : EventDAO.getInstance().findOrganizers(event)) {
			UserData organizerData = new UserData();
			organizerData.id = organizer.getId();
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
	@Path("{slug: [\\w\\d_\\-/]+}/banner/base64")
	public byte[] getBannerBase64(@PathParam("slug") String slug) throws Exception {
		return getBannerBase64(slug, null);
	}

	@GET
	@Produces("image/png")
	// @Cache("max-age=604800000")
	@Path("{slug: [\\w\\d_\\-/]+}/banner/base64/{width}")
	public byte[] getBannerBase64(@PathParam("slug") String slug, @PathParam("width") Integer width) throws Exception {
		Event race = loadEventBanner(slug);
		return Base64.encodeBase64(resizeImage(race.getBanner(), 750, width));
	}

	@GET
	@Produces("image/png")
	// @Cache("max-age=604800000")
	@Path("{slug: [\\w\\d_\\-/]+}/banner")
	public byte[] getBanner(@PathParam("slug") String slug) throws Exception {
		return getBanner(slug, null);
	}

	@GET
	@Produces("image/png")
	// @Cache("max-age=604800000")
	@Path("{slug: [\\w\\d_\\-/]+}/banner/{width}")
	public byte[] getBanner(@PathParam("slug") String slug, @PathParam("width") Integer width) throws Exception {
		Event race = loadEventBanner(slug);
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

	private Event loadEventDetails(String slug) throws Exception {
		Event result = EventDAO.getInstance().loadForDetail(slug);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Event loadEventBanner(String slug) throws Exception {
		Event result = EventDAO.getInstance().loadForBanner(slug);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}
}
