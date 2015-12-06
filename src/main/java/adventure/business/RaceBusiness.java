package adventure.business;

import java.util.Date;
import java.util.List;

import adventure.entity.Period;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.util.Beans;

public class RaceBusiness {

	public static RaceBusiness getInstance() {
		return Beans.getReference(RaceBusiness.class);
	}

	// public StatusType getStatus(Race race, Date date, List<Period> periods) {
	// StatusType result = null;
	// Period registrationPeriod = computePeriod(periods);
	//
	// if (Dates.before(date, registrationPeriod.getBeginning())) {
	// result = SOON;
	// } else if (Dates.between(date, registrationPeriod.getBeginning(), registrationPeriod.getEnd())) {
	// result = OPEN;
	// } else if (Dates.after(date, registrationPeriod.getEnd()) && Dates.beforeOrSame(date, race.getEnd())) {
	// result = CLOSED;
	// } else if (Dates.after(date, race.getEnd())) {
	// result = END;
	// }
	//
	// return result;
	// }

	public Period getPeriod(Date date, List<Period> periods) {
		Period result = null;

		for (int i = 0; i < periods.size(); i++) {
			Period period = periods.get(i);

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

	// private Period computePeriod(List<Period> periods) {
	// Date beginning = null;
	// Date end = null;
	//
	// for (Period period : periods) {
	// if (beginning == null || Dates.before(period.getBeginning(), beginning)) {
	// beginning = period.getBeginning();
	// }
	//
	// if (end == null || Dates.after(period.getEnd(), end)) {
	// end = period.getEnd();
	// }
	// }
	//
	// Period result = new Period();
	// result.setBeginning(beginning);
	// result.setEnd(end);
	// return result;
	// }
}
