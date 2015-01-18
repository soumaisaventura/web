package adventure.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@IdClass(RaceOrganizerPk.class)
@Table(name = "RACE_ORGANIZER")
public class RaceOrganizer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "RACE_ID")
	@ForeignKey(name = "FK_RACE_ORGANIZER_RACE")
	@Index(name = "IDX_RACE_ORGANIZER_RACE")
	private Race race;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "ORGANIZER_ID")
	@ForeignKey(name = "FK_RACE_ORGANIZER")
	@Index(name = "IDX_RACE_ORGANIZER")
	private User organizer;

	public RaceOrganizer() {
	}

	public RaceOrganizer(Race race, User organizer) {
		this.race = race;
		this.organizer = organizer;
	}

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
		if (!(obj instanceof RaceOrganizer)) {
			return false;
		}
		RaceOrganizer other = (RaceOrganizer) obj;
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

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public User getOrganizer() {
		return organizer;
	}

	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}
}
