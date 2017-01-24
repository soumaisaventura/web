package core.business;

import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Beans;
import core.entity.RaceCategory;
import core.entity.RegistrationPeriod;
import core.persistence.RaceCategoryDAO;
import core.util.Dates;

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
        RegistrationPeriod last = null;

        if (periods.size() > 0) {
            last = periods.get(periods.size() - 1);

            if (Dates.after(date, last.getEnd())) {
                result = last;

            } else {
                for (int i = 0; i < periods.size(); i++) {
                    RegistrationPeriod period = periods.get(i);

                    if (Dates.between(date, period.getBeginning(), period.getEnd())) {
                        result = period;
                        break;
                    }
                }
            }
        }

        return result;
    }
}
