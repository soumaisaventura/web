package adventure.rest;

import adventure.business.RaceBusiness;
import adventure.entity.*;
import adventure.persistence.*;
import adventure.rest.data.*;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.util.Cache;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;
import static adventure.util.Constants.RACE_SLUG_PATTERN;

@Path("events/{eventAlias: " + EVENT_SLUG_PATTERN + "}/races/{raceAlias: " + RACE_SLUG_PATTERN + "}")
public class RaceREST {

    @GET
    @Produces("application/json")
    public RaceData loadSummary(@PathParam("raceAlias") String raceAlias, @PathParam("eventAlias") String eventAlias,
                                @Context UriInfo uriInfo) throws Exception {
        RaceData data = new RaceData();
        Race race = loadRaceDetails(raceAlias, eventAlias);

        data.id = race.getAlias();
        data.internalId = race.getId();
        data.name = race.getName();
        data.description = race.getDescription();
        data.status = race.getStatus().getName();

        data.period = new PeriodData();
        data.period.beginning = race.getPeriod().getBeginning();
        data.period.end = race.getPeriod().getEnd();

        data.event = new EventData(uriInfo);
        data.event.id = race.getEvent().getAlias();
        data.event.internalId = race.getEvent().getId();
        data.event.name = race.getEvent().getName();
        data.event.site = race.getEvent().getSite();

        data.event.location = new LocationData();
        data.event.location.city = new CityData(race.getEvent().getCity());

        List<RegistrationPeriod> periods = PeriodDAO.getInstance().findForEvent(race);
        RegistrationPeriod currentPrice = RaceBusiness.getInstance().getPeriod(new Date(), periods);
        if (currentPrice != null) {
            data.currentPrice = new PeriodData();
            data.currentPrice.beginning = currentPrice.getBeginning();
            data.currentPrice.end = currentPrice.getEnd();
            data.currentPrice.price = currentPrice.getPrice();
        }

        data.event.organizers = new ArrayList();
        for (User organizer : UserDAO.getInstance().findOrganizers(race.getEvent())) {
            UserData organizerData = new UserData(uriInfo);
            organizerData.id = organizer.getId();
            organizerData.email = organizer.getEmail();

            organizerData.profile = new ProfileData();
            organizerData.profile.name = organizer.getProfile().getName();
            organizerData.profile.mobile = organizer.getProfile().getMobile();

            data.event.organizers.add(organizerData);
        }


        data.categories = new ArrayList<>();
        for (Category category : CategoryDAO.getInstance().find(race)) {
            CategoryData categoryData = new CategoryData(category);
            data.categories.add(categoryData);
        }

        data.kits = new ArrayList<>();
        for (Kit kit : KitDAO.getInstance().findForRegistration(race)) {
            KitData kitData = new KitData(kit);
            data.kits.add(kitData);
        }

        return data;
    }

    @GET
    @Path("ranking")
    @Cache("max-age=28800")
    @Produces("application/json")
    public List<RankingData> getRanking() {
        return null;
    }

    @POST
    @Path("ranking")
    @Consumes("application/json")
    public void setRanking(List<RankingData> datas) {
    }

    @GET
    @Path("ranking/category")
    @Cache("max-age=28800")
    @Produces("application/json")
    public List<RankingData> getRankingCatgegory() {
        return null;
    }

    private Race loadRaceDetails(String raceAlias, String eventAlias) throws Exception {
        Race result = RaceDAO.getInstance().loadForDetail(raceAlias, eventAlias);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }
}
