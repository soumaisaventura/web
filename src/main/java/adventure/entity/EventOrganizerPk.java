package adventure.entity;

import java.io.Serializable;

public class EventOrganizerPk implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer event;

	Integer organizer;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((organizer == null) ? 0 : organizer.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
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
		if (!(obj instanceof EventOrganizerPk)) {
			return false;
		}
		EventOrganizerPk other = (EventOrganizerPk) obj;
		if (organizer == null) {
			if (other.organizer != null) {
				return false;
			}
		} else if (!organizer.equals(other.organizer)) {
			return false;
		}
		if (event == null) {
			if (other.event != null) {
				return false;
			}
		} else if (!event.equals(other.event)) {
			return false;
		}
		return true;
	}
}
