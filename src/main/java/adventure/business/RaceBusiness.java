package adventure.business;

import java.util.Date;
import java.util.List;

import adventure.entity.RegistrationPeriod;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.util.Beans;

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
			} else if (i == 0 && Dates.before(date, period.getBeginning())) {
				result = period;
			} else if (i == periods.size() - 1 && Dates.after(date, period.getEnd())) {
				result = period;
			}

			if (result != null) {
				break;
			}
		}

		return result;
	}
}
