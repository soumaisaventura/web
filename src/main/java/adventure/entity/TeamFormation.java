package adventure.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@IdClass(TeamFormationPk.class)
@Table(name = "TEAM_FORMATION")
public class TeamFormation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "REGISTER_ID")
	@ForeignKey(name = "FK_TEAM_FORMATION_REGISTER")
	@Index(name = "IDX_TEAM_FORMATION_REGISTER")
	private Register register;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "USER_ID")
	@ForeignKey(name = "FK_TEAM_FORMATION_USER")
	@Index(name = "IDX_TEAM_FORMATION_USER")
	private User user;

	@NotNull
	@Column(name = "CONFIRMED")
	@Index(name = "IDX_TEAM_FORMATION_CONFIRMED")
	private boolean confirmed;

	public TeamFormation() {
	}

	public TeamFormation(Register register, User user) {
		this.register = register;
		this.user = user;
		this.confirmed = false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((register == null) ? 0 : register.hashCode());
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
		if (register == null) {
			if (other.register != null) {
				return false;
			}
		} else if (!register.equals(other.register)) {
			return false;
		}
		return true;
	}

	public Register getRegister() {
		return register;
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
}
