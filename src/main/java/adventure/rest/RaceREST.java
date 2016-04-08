package adventure.rest;

import adventure.entity.*;
import adventure.persistence.*;
import adventure.rest.data.*;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Cache;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;
import static adventure.util.Constants.RACE_SLUG_PATTERN;

@Path("events/{eventSlug: " + EVENT_SLUG_PATTERN + "}/{raceSlug: " + RACE_SLUG_PATTERN + "}")
public class RaceREST {

    @GET
    @Path("summary")
    @Produces("application/json")
    public RaceData loadSummary(@PathParam("raceSlug") String raceSlug, @PathParam("eventSlug") String eventSlug,
                                @Context UriInfo uriInfo) throws Exception {
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

        data.event = new EventData(uriInfo);
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

    @GET
    @Path("categories")
    @Produces("application/json")
    public List<CategoryData> findCategories(@PathParam("raceSlug") String raceSlug,
                                             @PathParam("eventSlug") String eventSlug) throws Exception {
        List<CategoryData> result = new ArrayList<CategoryData>();
        Race race = loadRaceDetails(raceSlug, eventSlug);

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

        return result.isEmpty() ? null : result;
    }

    @GET
    @Path("kits")
    @Produces("application/json")
    public List<KitData> getKits(@PathParam("raceSlug") String raceSlug,
                                 @PathParam("eventSlug") String eventSlug) throws Exception {
        List<KitData> result = new ArrayList<KitData>();
        Race race = loadRaceDetails(raceSlug, eventSlug);

        for (Kit kit : KitDAO.getInstance().findForRegistration(race)) {
            KitData kitData = new KitData();
            kitData.id = kit.getSlug();
            kitData.internalId = kit.getId();
            kitData.name = kit.getName();
            kitData.description = kit.getDescription();
            kitData.price = kit.getPrice();
            result.add(kitData);
        }

        return result.isEmpty() ? null : result;
    }

    @GET
    @Path("order")
    @Produces("application/json")
    public List<UserData> getOrder(@PathParam("raceSlug") String raceSlug, @PathParam("eventSlug") String eventSlug,
                                   @QueryParam("users_ids") List<Integer> users, @Context UriInfo uriInfo) throws Exception {
        Race race = loadRaceDetails(raceSlug, eventSlug);

        RegistrationPeriod period = PeriodDAO.getInstance().loadCurrent(race);
        List<UserData> result = new ArrayList<UserData>();

        if (users.isEmpty()) {
            throw new UnprocessableEntityException().addViolation("users", "parâmetro obrigatório");

        } else if (period != null) {
            for (Integer userId : users) {
                User user = UserDAO.getInstance().loadBasics(userId);

                if (user == null) {
                    throw new UnprocessableEntityException().addViolation("users", "usuário inválido");
                } else {
                    UserData row = new UserData(uriInfo);
                    row.id = user.getId();
                    row.name = user.getProfile().getName();
                    row.racePrice = period.getPrice();
                    result.add(row);
                }
            }

            period.setRace(null);
        }

        return result.isEmpty() ? null : result;
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

    private Race loadRaceDetails(String raceSlug, String eventSlug) throws Exception {
        Race result = RaceDAO.getInstance().loadForDetail(raceSlug, eventSlug);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }
}
