package adventure.entity;

import static adventure.util.Constants.EMAIL_SIZE;
import static adventure.util.Constants.NAME_SIZE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@IdClass(EventOrganizerPk.class)
@Table(name = "event_organizer")
public class EventOrganizer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;

	@Id
	@ManyToOne
	@JoinColumn(name = "organizer_id")
	private User organizer;

	@NotNull
	@Size(max = NAME_SIZE)
	@Column(name = "alternate_name")
	private String alternateName;

	@Size(max = EMAIL_SIZE)
	@Column(name = "alternate_email")
	private String alternateEmail;

	public EventOrganizer() {
	}

	public EventOrganizer(Event event, User organizer) {
		this.event = event;
		this.organizer = organizer;
	}

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
		if (!(obj instanceof EventOrganizer)) {
			return false;
		}
		EventOrganizer other = (EventOrganizer) obj;
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

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	public String getAlternateName() {
		return alternateName;
	}

	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}
}
