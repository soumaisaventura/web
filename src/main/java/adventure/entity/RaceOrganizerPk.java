package adventure.entity;

import java.io.Serializable;

public class RaceOrganizerPk implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer race;

	Integer organizer;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((organizer == null) ? 0 : organizer.hashCode());
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
		if (!(obj instanceof RaceOrganizerPk)) {
			return false;
		}
		RaceOrganizerPk other = (RaceOrganizerPk) obj;
		if (organizer == null) {
			if (other.organizer != null) {
				return false;
			}
		} else if (!organizer.equals(other.organizer)) {
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
