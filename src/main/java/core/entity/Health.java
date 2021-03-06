package core.entity;

import core.util.PendencyCount;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "health")
@SuppressWarnings("serial")
public class Health implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    @PendencyCount
    @Enumerated(STRING)
    @Column(name = "blood_type")
    private BloodType bloodType;

    private String allergy;

    @Column(name = "health_care_name")
    private String healthCareName;

    @Column(name = "health_care_number")
    private String healthCareNumber;

    @PendencyCount
    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @PendencyCount
    @Column(name = "emergency_contact_phone_number")
    private String emergencyContactPhoneNumber;

    @NotNull
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
