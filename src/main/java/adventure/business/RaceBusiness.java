package adventure.business;

import adventure.entity.RegistrationPeriod;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.util.Beans;

import java.util.Date;
import java.util.List;

public class RaceBusiness {

    public static RaceBusiness getInstance() {
        return Beans.getReference(RaceBusiness.class);
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
