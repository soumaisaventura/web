//package adventure.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//@Entity
//@IdClass(RaceOrganizerPk.class)
//@Table(name = "race_organizer")
//public class RaceOrganizer {
//
//	@Id
//	@ManyToOne
//	@JoinColumn(name = "race_id")
//	private Race race;
//
//	@Id
//	@ManyToOne
//	@JoinColumn(name = "organizer_id")
//	private User organizer;
//
//	@Column(name = "alternate_name")
//	private String alternateName;
//
//	@Column(name = "alternate_email")
//	private String alternateEmail;
//
//	public RaceOrganizer() {
//	}
//
//	public RaceOrganizer(Race race, User organizer) {
//		this.race = race;
//		this.organizer = organizer;
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((organizer == null) ? 0 : organizer.hashCode());
//		result = prime * result + ((race == null) ? 0 : race.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (obj == null) {
//			return false;
//		}
//		if (!(obj instanceof RaceOrganizer)) {
//			return false;
//		}
//		RaceOrganizer other = (RaceOrganizer) obj;
//		if (organizer == null) {
//			if (other.organizer != null) {
//				return false;
//			}
//		} else if (!organizer.equals(other.organizer)) {
//			return false;
//		}
//		if (race == null) {
//			if (other.race != null) {
//				return false;
//			}
//		} else if (!race.equals(other.race)) {
//			return false;
//		}
//		return true;
//	}
//
//	public Race getRace() {
//		return race;
//	}
//
//	public void setRace(Race race) {
//		this.race = race;
//	}
//
//	public User getOrganizer() {
//		return organizer;
//	}
//
//	public void setOrganizer(User organizer) {
//		this.organizer = organizer;
//	}
//
//	public String getAlternateName() {
//		return alternateName;
//	}
//
//	public void setAlternateName(String alternateName) {
//		this.alternateName = alternateName;
//	}
//
//	public String getAlternateEmail() {
//		return alternateEmail;
//	}
//
//	public void setAlternateEmail(String alternateEmail) {
//		this.alternateEmail = alternateEmail;
//	}
//}
