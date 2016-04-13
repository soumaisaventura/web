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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static adventure.util.Constants.EVENT_SLUG_PATTERN;
import static adventure.util.Constants.RACE_SLUG_PATTERN;

@Path("events/{eventAlias: " + EVENT_SLUG_PATTERN + "}/{raceAlias: " + RACE_SLUG_PATTERN + "}")
public class RaceREST {

    @GET
    @Path("summary")
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
        data.event.location.city = new CityData();
        data.event.location.city.id = race.getEvent().getCity().getId();
        data.event.location.city.name = race.getEvent().getCity().getName();
        data.event.location.city.state = race.getEvent().getCity().getState().getAbbreviation();

        return data;
    }

    @GET
    @Path("categories")
    @Produces("application/json")
    public List<CategoryData> findCategories(@PathParam("raceAlias") String raceAlias,
                                             @PathParam("eventAlias") String eventAlias) throws Exception {
        List<CategoryData> result = new ArrayList();
        Race race = loadRaceDetails(raceAlias, eventAlias);

        for (Category category : CategoryDAO.getInstance().find(race)) {
            CategoryData categoryData = new CategoryData();
            categoryData.id = category.getAlias();
            categoryData.internalId = category.getId();
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
    public List<KitData> getKits(@PathParam("raceAlias") String raceAlias,
                                 @PathParam("eventAlias") String eventAlias) throws Exception {
        List<KitData> result = new ArrayList();
        Race race = loadRaceDetails(raceAlias, eventAlias);

        for (Kit kit : KitDAO.getInstance().findForRegistration(race)) {
            KitData kitData = new KitData();
            kitData.id = kit.getAlias();
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
    public BigDecimal getOrder(@PathParam("raceAlias") String raceAlias, @PathParam("eventAlias") String eventAlias,
                               @QueryParam("user_id") Integer userId, @Context UriInfo uriInfo) throws Exception {
        Race race = loadRaceDetails(raceAlias, eventAlias);
        RegistrationPeriod period = PeriodDAO.getInstance().loadCurrent(race);
        User user;

        if (userId == null) {
            throw new UnprocessableEntityException().addViolation("user_id", "parâmetro obrigatório");

        } else {
            user = UserDAO.getInstance().loadBasics(userId);

            if (user == null) {
                throw new UnprocessableEntityException().addViolation("users", "usuário inválido");
            }
        }

        BigDecimal result;

        if (period != null) {
            result = period.getPrice();
        } else {
            throw new UnprocessableEntityException().addViolation("Fora do período de inscrição.");
        }

        return result;
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
