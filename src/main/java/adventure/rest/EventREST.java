package adventure.rest;

import adventure.business.EventBusiness;
import adventure.business.ImageBusiness;
import adventure.business.RaceBusiness;
import adventure.entity.*;
import adventure.persistence.*;
import adventure.rest.data.*;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Cache;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;

@Path("events")
public class EventREST {

    @GET
    @Path("year/{year : \\d+}")
    @Cache("max-age=28800")
    @Produces("application/json")
    public List<EventData> year(@PathParam("year") Integer year, @Context UriInfo uriInfo) throws Exception {
        List<EventData> result = new ArrayList();

        for (Event event : EventDAO.getInstance().findByYear(year)) {
            EventData eventData = new EventData(uriInfo);

            eventData.id = event.getAlias();
            eventData.internalId = event.getId();
            eventData.name = event.getName();
            eventData.period = new PeriodData();
            eventData.period.beginning = event.getBeginning();
            eventData.period.end = event.getEnd();
            eventData.status = event.getStatus().getName();
            eventData.location = new LocationData();
            eventData.location.city = new CityData(event.getCity());
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
        RaceDAO raceDAO = RaceDAO.getInstance();

        EventData eventData = new EventData(uriInfo);
        Event event = loadEventDetails(slug);
        Date now = new Date();

        eventData.id = event.getAlias();
        eventData.internalId = event.getId();
        eventData.name = event.getName();
        eventData.description = event.getDescription();
        eventData.site = event.getSite();
        eventData.period = new PeriodData();
        eventData.period.beginning = event.getBeginning();
        eventData.period.end = event.getEnd();
        eventData.status = event.getStatus().getName();

        // Location

        eventData.location = new LocationData();
        eventData.location.city = new CityData(event.getCity());

        // Hotspot

        eventData.location.hotspots = new ArrayList();
        for (Hotspot hotspot : HotspotDAO.getInstance().find(event)) {
            HotspotData hotspotData = new HotspotData();
            hotspotData.id = hotspot.getId();
            hotspotData.name = hotspot.getName();
            hotspotData.description = hotspot.getDescription();
            hotspotData.main = hotspot.getMain();
            hotspotData.coord = new CoordData();
            hotspotData.coord.latitude = hotspot.getCoord().getLatitude();
            hotspotData.coord.longitude = hotspot.getCoord().getLongitude();
            hotspotData.order = hotspot.getOrder();
            eventData.location.hotspots.add(hotspotData);
        }

        if (eventData.location.hotspots.isEmpty()) {
            eventData.location.hotspots = null;
        }

        if (!summary) {
            // Races

            ChampionshipDAO championshipDAO = ChampionshipDAO.getInstance();
            PeriodDAO periodDAO = PeriodDAO.getInstance();
            ModalityDAO modalityDAO = ModalityDAO.getInstance();
            RaceCategoryDAO raceCategoryDAO = RaceCategoryDAO.getInstance();
            KitDAO kitDAO = KitDAO.getInstance();

            eventData.races = new ArrayList();
            for (Race race : raceDAO.findForEvent(event)) {
                RaceData raceData = new RaceData();
                raceData.id = race.getAlias();
                raceData.internalId = race.getId();
                raceData.name = race.getName();
                raceData.description = race.getDescription();
                raceData.distance = race.getDistance();
                raceData.status = race.getStatus().getName();

                // Sport

                raceData.sport = new SportData();
                raceData.sport.id = race.getSport().getAlias();
                raceData.sport.internalId = race.getSport().getId();
                raceData.sport.name = race.getSport().getName();

                // Period

                raceData.period = new PeriodData();
                raceData.period.beginning = race.getPeriod().getBeginning();
                raceData.period.end = race.getPeriod().getEnd();

                raceData.championships = new ArrayList();
                for (Championship championship : championshipDAO.findForEvent(race)) {
                    ChampionshipData championshipData = new ChampionshipData();
                    championshipData.id = championship.getAlias();
                    championshipData.internalId = championship.getId();
                    championshipData.name = championship.getName();
                    raceData.championships.add(championshipData);
                }

                // Categories

                raceData.categories = new ArrayList<>();
                for (RaceCategory raceCategory : raceCategoryDAO.find(race)) {
                    CategoryData categoryData = new CategoryData(raceCategory.getCategory(), raceCategory.getVacant());
                    raceData.categories.add(categoryData);
                }

                // Kits

                raceData.kits = new ArrayList<>();
                for (Kit kit : kitDAO.findForRegistration(race)) {
                    KitData kitData = new KitData(kit);
                    raceData.kits.add(kitData);
                }

                // Registration Period + Prices

                List<RegistrationPeriod> periods = periodDAO.findForEvent(race);
                raceData.prices = new ArrayList();
                for (RegistrationPeriod period : periodDAO.findForEvent(race)) {
                    PeriodData periodData = new PeriodData();
                    periodData.id = period.getId();
                    periodData.beginning = period.getBeginning();
                    periodData.end = period.getEnd();
                    periodData.price = period.getPrice();
                    raceData.prices.add(periodData);
                }

                // Current Period

                RegistrationPeriod currentPrice = raceBusiness.getPeriod(now, periods);
                if (currentPrice != null) {
                    raceData.currentPrice = new PeriodData(currentPrice);
                }

                // Modalities

                raceData.modalities = new ArrayList();
                for (Modality modality : modalityDAO.findForEvent(race)) {
                    ModalityData modalityData = new ModalityData();
                    modalityData.id = modality.getAlias();
                    modalityData.internalId = modality.getId();
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

        eventData.organizers = new ArrayList();
        for (User organizer : UserDAO.getInstance().findOrganizers(event)) {
            UserData organizerData = new UserData(uriInfo);
            organizerData.id = organizer.getId();
            organizerData.email = organizer.getEmail();

            organizerData.profile = new ProfileData();
            organizerData.profile.name = organizer.getProfile().getName();
            organizerData.profile.mobile = organizer.getProfile().getMobile();

            eventData.organizers.add(organizerData);
        }

        return eventData;
    }

    @GET
    @LoggedIn
    @Transactional
    @Produces("application/json")
    @Path("{slug: " + EVENT_SLUG_PATTERN + "}/registrations")
    public List<RegistrationData> getRegistrations(@PathParam("slug") String slug, @Context UriInfo uriInfo) throws Exception {
        List<RegistrationData> result = new ArrayList();
        Event event = loadEventDetails(slug);

        List<User> organizers = UserDAO.getInstance().findOrganizers(event);
        if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
            throw new ForbiddenException();
        }

        for (Registration registration : RegistrationDAO.getInstance().findToOrganizer(event)) {
            RegistrationData data = new RegistrationData();
            data.id = registration.getId();
            data.number = registration.getFormattedId();
            data.date = registration.getDate();
            data.status = registration.getStatus();

            data.team = new TeamData();
            data.team.name = registration.getTeamName();

            data.category = new CategoryData();
            data.category.id = registration.getRaceCategory().getCategory().getAlias();
            data.category.internalId = registration.getRaceCategory().getCategory().getId();
            data.category.name = registration.getRaceCategory().getCategory().getName();

            data.race = new RaceData();
            data.race.id = registration.getRaceCategory().getRace().getAlias();
            data.race.internalId = registration.getRaceCategory().getRace().getId();
            data.race.name = registration.getRaceCategory().getRace().getName();

            data.team.members = new ArrayList();
            for (UserRegistration teamFormation : registration.getUserRegistrations()) {
                UserData userData = new UserData(uriInfo);
                userData.id = teamFormation.getUser().getId();
                userData.email = teamFormation.getUser().getEmail();

                Profile profile = teamFormation.getUser().getProfile();
                userData.profile = new ProfileData();
                userData.profile.name = profile.getName();
                userData.profile.mobile = profile.getMobile();
                userData.profile.city = new CityData(profile.getCity());

                userData.amount = teamFormation.getAmount();

                data.team.members.add(userData);
            }

            result.add(data);
        }

        return result.isEmpty() ? null : result;
    }


    @GET
    @Produces("image/png")
    @Cache("max-age=60")
    @Path("{slug: " + EVENT_SLUG_PATTERN + "}/banner")
    public Response getBanner(@PathParam("slug") String slug, @HeaderParam("If-None-Match") String tag,
                              @QueryParam("width") Integer width, @Context ServletContext context) throws Exception {
        Response response;
        Event event = loadEventBanner(slug);
        String persistedTag = event.getBannerHash();

        if (persistedTag.equals(tag)) {
            response = Response.notModified(persistedTag).build();
        } else {
            byte[] entity = ImageBusiness.getInstance().resize(loadBanner(event, context), 750, width);
            response = Response.ok(entity).tag(persistedTag).build();
        }

        return response;
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
    public void setBanner(@PathParam("slug") String slug, @NotEmpty InputStream inputStream) throws Exception {
        Event event = loadEvent(slug);
        checkPermission(event);

        if (inputStream == null) {
            throw new UnprocessableEntityException().addViolation("arquivo obrigatório");
        }

        EventBusiness.getInstance().updateBanner(event, new Picture(inputStream, "image/png"));
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
