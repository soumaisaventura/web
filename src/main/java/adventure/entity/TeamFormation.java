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
@IdClass(TeamFormationPk.class)
@Table(name = "TEAM_FORMATION")
public class TeamFormation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "REGISTRATION_ID")
	@ForeignKey(name = "FK_TEAM_FORMATION_REGISTRATION")
	@Index(name = "IDX_TEAM_FORMATION_REGISTRATION")
	private Registration registration;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "USER_ID")
	@ForeignKey(name = "FK_TEAM_FORMATION_USER")
	@Index(name = "IDX_TEAM_FORMATION_USER")
	private User user;

	public TeamFormation() {
	}

	public TeamFormation(Registration registration, User user) {
		this.registration = registration;
		this.user = user;
	}

	public TeamFormation(Integer userId, Long registrationId, String registrationTeamName) {
		setUser(new User());
		getUser().setId(userId);

		setRegistration(new Registration());
		getRegistration().setId(registrationId);
		getRegistration().setTeamName(registrationTeamName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((registration == null) ? 0 : registration.hashCode());
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
		if (!(obj instanceof TeamFormation)) {
			return false;
		}
		TeamFormation other = (TeamFormation) obj;
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		if (registration == null) {
			if (other.registration != null) {
				return false;
			}
		} else if (!registration.equals(other.registration)) {
			return false;
		}
		return true;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
