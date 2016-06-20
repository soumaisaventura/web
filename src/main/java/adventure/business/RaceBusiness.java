package adventure.business;

import adventure.entity.RaceCategory;
import adventure.entity.RegistrationPeriod;
import adventure.persistence.RaceCategoryDAO;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Beans;

import java.util.Date;
import java.util.List;

public class RaceBusiness {

    public static RaceBusiness getInstance() {
        return Beans.getReference(RaceBusiness.class);
    }

    public RaceCategory loadRaceCategory(Integer raceId, String categoryAlias) throws Exception {
        RaceCategory result = RaceCategoryDAO.getInstance().load(raceId, categoryAlias);

        if (result == null) {
            throw new UnprocessableEntityException().addViolation("category.id", "indisponível para esta prova");
        }

        return result;
    }

    public RegistrationPeriod getPeriod(Date date, List<RegistrationPeriod> periods) {
        RegistrationPeriod result = null;

        for (int i = 0; i < periods.size(); i++) {
            RegistrationPeriod period = periods.get(i);

            if (Dates.between(date, period.getBeginning(), period.getEnd())) {
                result = period;
                break;
            }
        }

        return result;
    }
}
