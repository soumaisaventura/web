package adventure.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@IdClass(UserRegistrationPk.class)
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

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "kit_id")
    private Kit kit;

    public UserRegistration() {
    }

    public UserRegistration(Registration registration, User user) {
        setRegistration(registration);
        setUser(user);
    }

    public UserRegistration(Integer userId, String userEmail, String profileName, String profileMobile,
                            TshirtType profileTshirt, Integer kitId, String kitName, Date profileBirthday, String profileRg, String profileCpf,
                            Integer cityId, String cityName, Integer stateId, String stateName, String stateAbbreviation, Integer countryId, String countryName, String countryAbbreviation,
                            String nationalId, String sicardNumber,
                            BigDecimal amount, Long registrationId,
                            RegistrationStatusType registrationStatus, String registrationTeamName, Date registrationDate,
                            Integer raceId, String raceAlias, String raceName,
                            Integer categoryId, String categoryAlias, String categoryName, String categoryDescription, Integer categoryTeamSize, Integer categoryMinMaleMembers,
                            Integer categoryMinFemaleMembers, Integer categoryMinMemberAge, Integer categoryMaxMemberAge, Integer categoryMinTeamAge, Integer categoryMaxTeamAge) {
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

        if (kitId != null || kitName != null) {
            getUser().setKit(new Kit());
            getUser().getKit().setId(kitId);
            getUser().getKit().setName(kitName);
        }

        getUser().getProfile().setCity(new City());
        getUser().getProfile().getCity().setId(cityId);
        getUser().getProfile().getCity().setName(cityName);
        getUser().getProfile().getCity().setState(new State());
        getUser().getProfile().getCity().getState().setId(stateId);
        getUser().getProfile().getCity().getState().setName(stateName);
        getUser().getProfile().getCity().getState().setAbbreviation(stateAbbreviation);
        getUser().getProfile().getCity().getState().setCountry(new Country());
        getUser().getProfile().getCity().getState().getCountry().setId(countryId);
        getUser().getProfile().getCity().getState().getCountry().setName(countryName);
        getUser().getProfile().getCity().getState().getCountry().setAbbreviation(countryAbbreviation);
        getUser().getProfile().setNationalId(nationalId);
        getUser().getProfile().setSicardNumber(sicardNumber);

        setAmount(amount);

        setRegistration(new Registration());
        getRegistration().setId(registrationId);
        getRegistration().setStatus(registrationStatus);
        getRegistration().setTeamName(registrationTeamName);
        getRegistration().setDate(registrationDate);

        getRegistration().setRaceCategory(new RaceCategory());
        getRegistration().getRaceCategory().setRace(new Race());
        getRegistration().getRaceCategory().getRace().setId(raceId);
        getRegistration().getRaceCategory().getRace().setAlias(raceAlias);
        getRegistration().getRaceCategory().getRace().setName(raceName);

        getRegistration().getRaceCategory().setCategory(new Category(categoryId, categoryAlias, categoryName, categoryDescription, categoryTeamSize, categoryMinMaleMembers, categoryMinFemaleMembers, categoryMinMemberAge, categoryMaxMemberAge, categoryMinTeamAge, categoryMaxTeamAge));
    }

    public UserRegistration(Long registrationId, Integer userId, String userEmail, String profileName,
                            GenderType profileGender, String profileMobile, BigDecimal amount,
                            Integer kitId, String kitAlias, String kitName, String kitDescription) {
        setRegistration(new Registration());
        getRegistration().setId(registrationId);

        setUser(new User());
        getUser().setId(userId);
        getUser().setEmail(userEmail);

        getUser().setProfile(new Profile());
        getUser().getProfile().setName(profileName);
        getUser().getProfile().setGender(profileGender);
        getUser().getProfile().setMobile(profileMobile);

        if (kitId != null) {
            setKit(new Kit());
            getKit().setId(kitId);
            getKit().setAlias(kitAlias);
            getKit().setName(kitName);
            getKit().setDescription(kitDescription);
        }

        setAmount(amount);
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }
}
