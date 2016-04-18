package adventure.rest;

import adventure.business.MailBusiness;
import adventure.entity.*;
import adventure.persistence.*;
import adventure.rest.data.RaceRegistrationData;
import adventure.rest.data.UserData;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static adventure.entity.GenderType.FEMALE;
import static adventure.entity.GenderType.MALE;
import static adventure.entity.RegistrationStatusType.PENDENT;
import static adventure.util.Constants.EVENT_SLUG_PATTERN;
import static adventure.util.Constants.RACE_SLUG_PATTERN;
import static java.math.BigDecimal.ZERO;

@Path("events/{eventAlias: " + EVENT_SLUG_PATTERN + "}/races/{raceAlias: " + RACE_SLUG_PATTERN + "}/registrations")
public class RaceRegistrationREST {

    @POST
    @LoggedIn
    @Transactional
    @ValidatePayload
    @Consumes("application/json")
    @Produces("application/json")
    public Response submit(@PathParam("raceAlias") String raceAlias, @PathParam("eventAlias") String eventAlias,
                           RaceRegistrationData data, @Context UriInfo uriInfo) throws Exception {
        Race race = loadRace(raceAlias, eventAlias);
        Date date = new Date();
        URI baseUri = uriInfo.getBaseUri().resolve("..");

        User submitter = UserDAO.getInstance().loadBasics(User.getLoggedIn().getEmail());
        RaceCategory raceCategory = loadRaceCategory(race.getId(), data.category.id);
        List<User> members = loadMembers(raceCategory.getRace(), data.team.members);
        RegistrationPeriod period = loadPeriod(raceCategory.getRace(), date);
        validate(raceCategory, members, submitter, data.team.name, baseUri);

        Registration result = submit(data, raceCategory, members, date, period, submitter);
        MailBusiness.getInstance().sendRegistrationCreation(result, baseUri);

        URI location = uriInfo.getBaseUri().resolve("registrations/" + result.getId());
        return Response.created(location).entity(result.getFormattedId()).build();
    }

    private Registration submit(RaceRegistrationData data, RaceCategory raceCategory, List<User> members, Date date,
                                RegistrationPeriod period, User submitter) {
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
            UserRegistration userRegistration = new UserRegistration();
            userRegistration.setRegistration(registration);
            userRegistration.setUser(attachedMember);
            userRegistration.setKit(member.getKit());

            BigDecimal registrationPrice = period.getPrice();
            BigDecimal kitPrice = member.getKit() == null ? ZERO : member.getKit().getPrice();
            userRegistration.setAmount(registrationPrice.add(kitPrice));

            UserRegistrationDAO.getInstance().insert(userRegistration);
            result.getUserRegistrations().add(userRegistration);
        }

        return result;
    }

    private Race loadRace(String raceAlias, String eventAlias) throws Exception {
        Race result = RaceDAO.getInstance().loadForDetail(raceAlias, eventAlias);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }

    private RaceCategory loadRaceCategory(Integer raceId, String categoryAlias) throws Exception {
        RaceCategory result = RaceCategoryDAO.getInstance().load(raceId, categoryAlias);

        if (result == null) {
            throw new UnprocessableEntityException().addViolation("category.id", "indisponível para esta prova");
        }

        return result;
    }

    private List<User> loadMembers(Race race, List<UserData> members) throws Exception {
        UserDAO userDAO = UserDAO.getInstance();
        KitDAO kitDAO = KitDAO.getInstance();

        List<User> result = new ArrayList();
        UnprocessableEntityException exception = new UnprocessableEntityException();

        for (UserData member : members) {
            User user = userDAO.loadBasics(member.id);
            Kit kit = null;

            if (user == null) {
                exception.addViolation("Usuário " + member.id + " inválido.");
            } else if (result.contains(user)) {
                exception.addViolation("Usuário " + member.id + " duplicado.");
            } else {
                if (member.kit != null) {
                    kit = kitDAO.loadForRegistration(race, member.kit.id);

                    if (kit == null) {
                        exception.addViolation("Kit " + member.kit.id + " inválido.");
                    }
                }

                user.setKit(kit);
                result.add(user);
            }
        }

        if (!exception.getViolations().isEmpty()) {
            throw exception;
        }

        return result;
    }


    private List<Kit> loadKits(Race race) throws Exception {
        return KitDAO.getInstance().findForRegistration(race);
    }

    private RegistrationPeriod loadPeriod(Race race, Date date) throws Exception {
        RegistrationPeriod result = PeriodDAO.getInstance().load(race, date);

        if (result == null && User.getLoggedIn().getAdmin()) {
            List<RegistrationPeriod> periods = PeriodDAO.getInstance().findForEvent(race);
            result = periods != null && !periods.isEmpty() ? periods.get(periods.size() - 1) : null;
        }

        if (result == null) {
            throw new UnprocessableEntityException().addViolation("Fora do período de inscrição.");
        }

        return result;
    }

    private void validate(RaceCategory raceCategory, List<User> members, User submitter, String teamName, URI baseUri)
            throws Exception {
        int total = members.size();
        UnprocessableEntityException exception = new UnprocessableEntityException();
        Category category = raceCategory.getCategory();
        List<Kit> kits = loadKits(raceCategory.getRace());

        if (total > category.getTeamSize()) {
            exception.addViolation("Tem muita gente na equipe.");
        } else if (total < category.getTeamSize()) {
            exception.addViolation("A equipe está incompleta.");
        }

        int male = count(members, MALE);
        if (category.getMinMaleMembers() != null && male < category.getMinMaleMembers()) {
            exception.addViolation("Falta atleta do sexo masculino.");
        }

        int female = count(members, FEMALE);
        if (category.getMinFemaleMembers() != null && female < category.getMinFemaleMembers()) {
            exception.addViolation("Falta atleta do sexo feminino.");
        }

        for (User member : members) {
            UserRegistration formation = UserRegistrationDAO.getInstance().loadForRegistrationSubmissionValidation(
                    raceCategory.getRace(), member);

            if (formation != null) {
                exception.addViolation(parse(member) + " já faz parte da equipe "
                        + formation.getRegistration().getTeamName() + ".");
            }

            if (member.getProfile().getPendencies() > 0 || member.getHealth().getPendencies() > 0) {
                exception.addViolation(parse(member) + " possui pendências cadastrais.");
                MailBusiness.getInstance().sendRegistrationFailed(member, submitter, raceCategory, teamName, baseUri);
            }

            if (member.getKit() == null && !kits.isEmpty()) {
                exception.addViolation(parse(member) + " está sem kit.");
            }
        }

        if (!exception.getViolations().isEmpty()) {
            throw exception;
        }
    }

    private String parse(User user) {
        String result;

        if (user.equals(User.getLoggedIn())) {
            result = "Você";
        } else {
            result = user.getName();
        }

        return result;
    }

    private int count(List<User> members, GenderType gender) {
        int result = 0;

        for (User user : members) {
            if (user.getProfile().getGender() == gender) {
                result++;
            }
        }

        return result;
    }
}
