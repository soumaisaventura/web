package adventure.rest;

import adventure.business.MailBusiness;
import adventure.entity.*;
import adventure.persistence.*;
import adventure.rest.data.RaceRegistrationData;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static adventure.entity.GenderType.FEMALE;
import static adventure.entity.GenderType.MALE;
import static adventure.entity.RegistrationStatusType.PENDENT;
import static adventure.util.Constants.EVENT_SLUG_PATTERN;
import static adventure.util.Constants.RACE_SLUG_PATTERN;

@Path("event/{eventSlug: " + EVENT_SLUG_PATTERN + "}/{raceSlug: " + RACE_SLUG_PATTERN + "}/registration")
public class RaceRegistrationREST {

    @POST
    @LoggedIn
    @Transactional
    @ValidatePayload
    @Produces("text/plain")
    @Consumes("application/json")
    public String submit(@PathParam("raceSlug") String raceSlug, @PathParam("eventSlug") String eventSlug,
                         RaceRegistrationData data, @Context UriInfo uriInfo) throws Exception {
        Race race = loadRace(raceSlug, eventSlug);
        Date date = new Date();
        URI baseUri = uriInfo.getBaseUri().resolve("..");

        User submitter = UserDAO.getInstance().loadBasics(User.getLoggedIn().getEmail());
        RaceCategory raceCategory = loadRaceCategory(race.getId(), data.categoryId);
        List<User> membersIds = loadMembers(data.membersIds);
        RegistrationPeriod period = loadPeriod(raceCategory.getRace(), date);
        validate(raceCategory, membersIds, submitter, data.teamName, baseUri);

        Registration result = submit(data, raceCategory, membersIds, date, period, submitter);

        MailBusiness.getInstance().sendRegistrationCreation(result, baseUri);
        return result.getFormattedId();
    }

    private Registration submit(RaceRegistrationData data, RaceCategory raceCategory, List<User> members, Date date,
                                RegistrationPeriod period, User submitter) {
        Registration result = null;
        Registration registration = new Registration();
        registration.setTeamName(data.teamName);
        registration.setRaceCategory(raceCategory);
        registration.setSubmitter(submitter);
        registration.setStatus(PENDENT);
        registration.setStatusDate(date);
        registration.setDate(date);
        registration.setPeriod(period);
        result = RegistrationDAO.getInstance().insert(registration);

        result.setUserRegistrations(new ArrayList<UserRegistration>());
        for (User member : members) {
            User atachedMember = UserDAO.getInstance().load(member.getId());
            UserRegistration teamFormation = new UserRegistration();
            teamFormation.setRegistration(registration);
            teamFormation.setUser(atachedMember);
            teamFormation.setRacePrice(period.getPrice());

            UserRegistrationDAO.getInstance().insert(teamFormation);
            result.getUserRegistrations().add(teamFormation);
        }

        return result;
    }

    private Race loadRace(String raceSlug, String eventSlug) throws Exception {
        Race result = RaceDAO.getInstance().loadForDetail(raceSlug, eventSlug);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }

    private RaceCategory loadRaceCategory(Integer raceId, Integer categoryId) throws Exception {
        RaceCategory result = RaceCategoryDAO.getInstance().load(raceId, categoryId);

        if (result == null) {
            throw new UnprocessableEntityException().addViolation("categoryId", "indisponível para esta prova");
        }

        return result;
    }

    private List<User> loadMembers(List<Integer> ids) throws Exception {
        List<User> result = new ArrayList<User>();
        UnprocessableEntityException exception = new UnprocessableEntityException();

        for (Integer id : ids) {
            User user = UserDAO.getInstance().loadBasics(id);

            if (user == null) {
                exception.addViolation("Usuário " + id + " inválido.");
            } else if (result.contains(user)) {
                exception.addViolation("Usuário " + id + " duplicado.");
            } else {
                result.add(user);
            }
        }

        if (!exception.getViolations().isEmpty()) {
            throw exception;
        }

        return result;
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
        }

        if (!exception.getViolations().isEmpty()) {
            throw exception;
        }
    }

    private String parse(User user) {
        String result = null;

        if (user.equals(User.getLoggedIn())) {
            result = "Você";
        } else {
            // result = Strings.firstToUpper(user.getName().split(" +")[0].toLowerCase());
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
