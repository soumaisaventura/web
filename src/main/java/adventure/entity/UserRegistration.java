package adventure.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(TeamFormationPk.class)
@Table(name = "user_registration")
public class UserRegistration {

	@Id
	@ManyToOne
	@JoinColumn(name = "registration_id")
	private Registration registration;

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "race_price")
	private BigDecimal racePrice;

	public UserRegistration() {
	}

	public UserRegistration(Registration registration, User user) {
		setRegistration(registration);
		setUser(user);
	}

	public UserRegistration(Integer userId, String userEmail, String profileName, String profileMobile,
			TshirtType profileTshirt, Date profileBirthday, String profileRg, String profileCpf, Integer cityId,
			String cityName, Integer stateId, String stateAbbreviation, BigDecimal racePrice, Long registrationId,
			RegistrationStatusType registrationStatus, String registrationTeamName, Date registrationDate,
			Integer raceId, String raceSlug, String raceName, Integer categoryId, String categoryName) {
		setUser(new User());
		getUser().setId(userId);
		getUser().setEmail(userEmail);

		getUser().setProfile(new Profile());
		getUser().getProfile().setName(profileName);
		getUser().getProfile().setMobile(profileMobile);
		getUser().getProfile().setTshirt(profileTshirt);
		getUser().getProfile().setBirthday(profileBirthday);
		getUser().getProfile().setRg(profileRg);
		getUser().getProfile().setCpf(profileCpf);

		getUser().getProfile().setCity(new City());
		getUser().getProfile().getCity().setId(cityId);
		getUser().getProfile().getCity().setName(cityName);
		getUser().getProfile().getCity().setState(new State());
		getUser().getProfile().getCity().getState().setId(stateId);
		getUser().getProfile().getCity().getState().setAbbreviation(stateAbbreviation);

		setRacePrice(racePrice);

		setRegistration(new Registration());
		getRegistration().setId(registrationId);
		getRegistration().setStatus(registrationStatus);
		getRegistration().setTeamName(registrationTeamName);
		getRegistration().setDate(registrationDate);

		getRegistration().setRaceCategory(new RaceCategory());
		getRegistration().getRaceCategory().setRace(new Race());
		getRegistration().getRaceCategory().getRace().setId(raceId);
		getRegistration().getRaceCategory().getRace().setSlug(raceSlug);
		getRegistration().getRaceCategory().getRace().setName(raceName);

		getRegistration().getRaceCategory().setCategory(new Category());
		getRegistration().getRaceCategory().getCategory().setId(categoryId);
		getRegistration().getRaceCategory().getCategory().setName(categoryName);
	}

	public UserRegistration(Long registrationId, Integer userId, String userEmail, String profileName,
			GenderType profileGender, String profileMobile, BigDecimal racePrice) {
		setRegistration(new Registration());
		getRegistration().setId(registrationId);

		setUser(new User());
		getUser().setId(userId);
		getUser().setEmail(userEmail);

		getUser().setProfile(new Profile());
		getUser().getProfile().setName(profileName);
		getUser().getProfile().setGender(profileGender);
		getUser().getProfile().setMobile(profileMobile);

		setRacePrice(racePrice);
	}

	public UserRegistration(Integer userId, Long registrationId, String registrationTeamName) {
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
		if (!(obj instanceof UserRegistration)) {
			return false;
		}
		UserRegistration other = (UserRegistration) obj;
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

	public BigDecimal getRacePrice() {
		return racePrice;
	}

	public void setRacePrice(BigDecimal racePrice) {
		this.racePrice = racePrice;
	}
}
