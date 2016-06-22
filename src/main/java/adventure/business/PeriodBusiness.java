package adventure.business;

import adventure.entity.Race;
import adventure.entity.RegistrationPeriod;
import adventure.entity.User;
import adventure.persistence.PeriodDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Beans;

import java.util.Date;
import java.util.List;

public class PeriodBusiness {

    public static PeriodBusiness getInstance() {
        return Beans.getReference(PeriodBusiness.class);
    }

    public RegistrationPeriod loadCurrent(Race race) throws Exception {
        return load(race, new Date());
    }

    public RegistrationPeriod load(Race race, Date date) throws Exception {
        RegistrationPeriod result = PeriodDAO.getInstance().load(race, date);

        List<User> organizers = UserDAO.getInstance().findOrganizers(race.getEvent());
        User loggedInUser = User.getLoggedIn();

        if (result == null && loggedInUser != null && (loggedInUser.getAdmin() || organizers.contains(loggedInUser))) {
            List<RegistrationPeriod> periods = PeriodDAO.getInstance().findForEvent(race);
            result = periods != null && !periods.isEmpty() ? periods.get(periods.size() - 1) : null;
        }

        if (result == null) {
            throw new UnprocessableEntityException().addViolation("Fora do período de inscrição.");
        }

        return result;
    }
}
