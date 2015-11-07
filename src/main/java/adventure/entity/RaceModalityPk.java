package adventure.entity;

import java.io.Serializable;

public class RaceModalityPk implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer race;

	Integer modality;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((modality == null) ? 0 : modality.hashCode());
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
		if (!(obj instanceof RaceModalityPk)) {
			return false;
		}
		RaceModalityPk other = (RaceModalityPk) obj;
		if (modality == null) {
			if (other.modality != null) {
				return false;
			}
		} else if (!modality.equals(other.modality)) {
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
