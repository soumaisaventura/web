package adventure.entity;

import java.io.Serializable;

public class RaceFeePk implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer race;

	Integer fee;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fee == null) ? 0 : fee.hashCode());
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
		if (!(obj instanceof RaceFeePk)) {
			return false;
		}
		RaceFeePk other = (RaceFeePk) obj;
		if (fee == null) {
			if (other.fee != null) {
				return false;
			}
		} else if (!fee.equals(other.fee)) {
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
