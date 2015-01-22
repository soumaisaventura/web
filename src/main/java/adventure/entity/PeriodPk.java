package adventure.entity;

import java.io.Serializable;
import java.util.Date;

public class PeriodPk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Race race;

	private Date beginning;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beginning == null) ? 0 : beginning.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PeriodPk)) {
			return false;
		}
		PeriodPk other = (PeriodPk) obj;
		if (beginning == null) {
			if (other.beginning != null) {
				return false;
			}
		} else if (!beginning.equals(other.beginning)) {
			return false;
		}
		if (race == null) {
			if (other.race != null) {
				return false;
			}
		} else if (!race.equals(other.race)) {
			return false;
		}
		return true;
	}
}
