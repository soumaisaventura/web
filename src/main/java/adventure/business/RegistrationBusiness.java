package adventure.business;

import adventure.entity.*;
import adventure.persistence.KitDAO;
import adventure.persistence.UserRegistrationDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Beans;

import java.net.URI;
import java.util.List;

import static adventure.entity.GenderType.FEMALE;
import static adventure.entity.GenderType.MALE;

public class RegistrationBusiness {

    public static RegistrationBusiness getInstance() {
        return Beans.getReference(RegistrationBusiness.class);
    }

    public void validate(Long id, RaceCategory raceCategory, String teamName, List<User> members, User submitter, URI baseUri)
            throws Exception {
        int total = members.size();
        UnprocessableEntityException exception = new UnprocessableEntityException();
        Category category = raceCategory.getCategory();
        List<Kit> kits = KitDAO.getInstance().findForRegistration(raceCategory.getRace());

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

            if (formation != null && !formation.getRegistration().getId().equals(id)) {
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
