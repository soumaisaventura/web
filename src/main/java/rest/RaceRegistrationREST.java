package rest;

import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import core.business.*;
import core.entity.*;
import core.persistence.RaceDAO;
import core.persistence.RegistrationDAO;
import core.persistence.UserDAO;
import core.persistence.UserRegistrationDAO;
import rest.data.RegistrationData;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static core.entity.RegistrationStatusType.PENDENT;
import static core.util.Constants.EVENT_SLUG_PATTERN;
import static core.util.Constants.RACE_SLUG_PATTERN;

@Path("events/{eventAlias: " + EVENT_SLUG_PATTERN + "}/races/{raceAlias: " + RACE_SLUG_PATTERN + "}/registrations")
public class RaceRegistrationREST {

    @POST
    @LoggedIn
    @Transactional
    @ValidatePayload
    @Consumes("application/json")
    @Produces("application/json")
    public Response submit(@PathParam("raceAlias") String raceAlias, @PathParam("eventAlias") String eventAlias,
                           RegistrationData data, @Context UriInfo uriInfo) throws Exception {
        Race race = loadRace(raceAlias, eventAlias);
        Date date = new Date();
        URI baseUri = uriInfo.getBaseUri().resolve("..");
        User submitter = UserDAO.getInstance().loadBasics(User.getLoggedIn().getEmail());

        RaceCategory raceCategory = RaceBusiness.getInstance().loadRaceCategory(race.getId(), data.category.id);
        List<User> members = UserBusiness.getInstance().loadMembers(raceCategory.getRace(), data.team.members);
        RegistrationPeriod period = PeriodBusiness.getInstance().load(raceCategory.getRace(), date);
        RegistrationBusiness.getInstance().validate(null, raceCategory, data.team.name, members, submitter, baseUri);

        Registration result = submit(data, raceCategory, members, date, period, submitter, baseUri);

        URI location = uriInfo.getBaseUri().resolve("registrations/" + result.getId());
        return Response.created(location).entity(result.getFormattedId()).build();
    }

    private Registration submit(RegistrationData data, RaceCategory raceCategory, List<User> members, Date date,
                                RegistrationPeriod period, User submitter, URI baseUri) throws Exception {
        Registration registration = new Registration();
        registration.setTeamName(data.team.name);
        registration.setRaceCategory(raceCategory);
        registration.setSubmitter(submitter);
        registration.setStatus(PENDENT);
        registration.setStatusDate(date);
        registration.setDate(date);
        registration.setPeriod(period);

        Registration result = RegistrationDAO.getInstance().insert(registration);
        result.setUserRegistrations(new ArrayList());

        for (User member : members) {
            User attachedMember = UserDAO.getInstance().load(member.getId());
            UserRegistration userRegistration = insert(registration, attachedMember, member.getKit(), period);
            result.getUserRegistrations().add(userRegistration);
        }

        MailBusiness.getInstance().sendRegistrationCreation(result.getId(), baseUri);
        return result;
    }

    private UserRegistration insert(Registration registration, User user, Kit kit, RegistrationPeriod period) {
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setRegistration(registration);
        userRegistration.setUser(user);
        userRegistration.setKit(kit);
        userRegistration.setAmount(period.getPrice().add(KitBusiness.getInstance().getPrice(kit)));

        return UserRegistrationDAO.getInstance().insert(userRegistration);
    }

    private Race loadRace(String raceAlias, String eventAlias) throws Exception {
        Race result = RaceDAO.getInstance().loadForDetail(raceAlias, eventAlias);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }
}
