package adventure.entity;

import adventure.util.PendencyCount;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "profile")
@SuppressWarnings("serial")
public class Profile implements Serializable {

    public static final String TEST_MOBILE = "(te) stest-este";

    public static final Date TEST_BIRTHDAY = new Date(0);

    public static final String TEST_RG = "testeste-st";

    public static final String TEST_CPF = "tes.tes.tes-te";

    @Id
    @OneToOne
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id")
    private User user;

    private String name;

    @Lob
    private byte[] picture;

    @Column(name = "_picture_hash")
    private String pictureHash;

    @PendencyCount
    @Temporal(DATE)
    private Date birthday;

    @PendencyCount
    private String mobile;

    @Enumerated(STRING)
    private GenderType gender;

    @PendencyCount
    @Enumerated(STRING)
    private TshirtType tshirt;

    @PendencyCount
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    private String rg;

    private String cpf;

    @Column(name = "orienteering_national_id")
    private String nationalId;

    @Column(name = "orienteering_sicard_number")
    private String sicardNumber;

    private Integer pendencies;

    public Profile() {
    }

    public Profile(String name, String rg, String cpf, Date birthday, String mobile, GenderType gender,
                   TshirtType tshirt, Integer pendencies, Integer userId, String userEmail, Integer cityId, String cityName,
                   Integer stateId, String stateName, String stateAbbreviation, Integer countryId, String countryName,
                   String countryAbbreviation, String nationalId, String sicardNumber) {
        setName(name);
        setRg(rg);
        setCpf(cpf);
        setBirthday(birthday);
        setMobile(mobile);
        setGender(gender);
        setTshirt(tshirt);
        setPendencies(pendencies);

        setUser(new User());
        getUser().setId(userId);
        getUser().setEmail(userEmail);

        setCity(new City());
        getCity().setId(cityId);
        getCity().setName(cityName);
        getCity().setState(new State());
        getCity().getState().setId(stateId);
        getCity().getState().setName(stateName);
        getCity().getState().setAbbreviation(stateAbbreviation);
        getCity().getState().setCountry(new Country());
        getCity().getState().getCountry().setId(countryId);
        getCity().getState().getCountry().setName(countryName);
        getCity().getState().getCountry().setAbbreviation(countryAbbreviation);

        setNationalId(nationalId);
        setSicardNumber(sicardNumber);
    }

    public Profile(User user) {
        this.user = user;
    }

    public Integer getAge() {
        return this.getAge(new Date());
    }

    public Integer getAge(Date date) {
        Integer result = null;

        if (this.getBirthday() != null) {
            //result = Years.yearsBetween(new LocalDate(this.getBirthday()), new LocalDate(date)).getYears();
            result = new LocalDate(date).getYear() - new LocalDate(this.getBirthday()).getYear();
        }

        return result;
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
        if (!(obj instanceof Profile)) {
            return false;
        }
        Profile other = (Profile) obj;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureHash() {
        return pictureHash;
    }

    public void setPictureHash(String pictureHash) {
        this.pictureHash = pictureHash;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public TshirtType getTshirt() {
        return tshirt;
    }

    public void setTshirt(TshirtType tshirt) {
        this.tshirt = tshirt;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getSicardNumber() {
        return sicardNumber;
    }

    public void setSicardNumber(String sicardNumber) {
        this.sicardNumber = sicardNumber;
    }

    public Integer getPendencies() {
        return pendencies;
    }

    public void setPendencies(Integer pendencies) {
        this.pendencies = pendencies;
    }
}
