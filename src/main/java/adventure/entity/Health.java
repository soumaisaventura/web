package adventure.entity;

import static adventure.util.Constants.ENUM_SIZE;
import static adventure.util.Constants.GENERIC_ID_SIZE;
import static adventure.util.Constants.NAME_SIZE;
import static adventure.util.Constants.TELEPHONE_SIZE;
import static adventure.util.Constants.TEXT_SIZE;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import adventure.util.PendencyCount;

@Entity
@Table(name = "health")
public class Health implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne
	@JoinColumn(name = "id")
	@ForeignKey(name = "fk_health_user")
	private User user;

	@PendencyCount
	@Enumerated(STRING)
	@Column(name = "blood_type", length = ENUM_SIZE)
	private BloodType bloodType;

	@Size(max = TEXT_SIZE)
	@Column(name = "allergy")
	private String allergy;

	@Size(max = NAME_SIZE)
	@Column(name = "health_care_name")
	private String healthCareName;

	@Size(max = GENERIC_ID_SIZE)
	@Column(name = "health_care_number")
	private String healthCareNumber;

	@PendencyCount
	@Size(max = NAME_SIZE)
	@Column(name = "emergency_contact_name")
	private String emergencyContactName;

	@PendencyCount
	@Size(max = TELEPHONE_SIZE)
	@Column(name = "emergency_contact_phone_number")
	private String emergencyContactPhoneNumber;

	@NotNull
	@Column(name = "pendencies")
	private Integer pendencies;

	public Health() {
	}

	public Health(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getHealthCareName() {
		return healthCareName;
	}

	public void setHealthCareName(String healthCareName) {
		this.healthCareName = healthCareName;
	}

	public String getHealthCareNumber() {
		return healthCareNumber;
	}

	public void setHealthCareNumber(String healthCareNumber) {
		this.healthCareNumber = healthCareNumber;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyContactPhoneNumber() {
		return emergencyContactPhoneNumber;
	}

	public void setEmergencyContactPhoneNumber(String emergencyContactPhoneNumber) {
		this.emergencyContactPhoneNumber = emergencyContactPhoneNumber;
	}

	public Integer getPendencies() {
		return pendencies;
	}

	public void setPendencies(Integer pendencies) {
		this.pendencies = pendencies;
	}
}
