package adventure.entity;

import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "HEALTH")
public class Health implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne
	@JoinColumn(name = "ID")
	@ForeignKey(name = "FK_HEALTH_ACCOUNT")
	private Account account;

	@Enumerated(STRING)
	@Column(name = "BLOOD_TYPE")
	private BloodType bloodType;

	@Column(name = "ALLERGY")
	private String allergy;

	@Column(name = "PENDENT", nullable = false)
	private boolean pendent = true;

	public Health() {
	}

	public Health(Account account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
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
		if (!(obj instanceof Health)) {
			return false;
		}
		Health other = (Health) obj;
		if (account == null) {
			if (other.account != null) {
				return false;
			}
		} else if (!account.equals(other.account)) {
			return false;
		}
		return true;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public BloodType getBloodType() {
		return bloodType;
	}

	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public boolean isPendent() {
		return pendent;
	}

	public void setPendent(boolean pendent) {
		this.pendent = pendent;
	}
}
