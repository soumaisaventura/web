package adventure.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import adventure.util.PendencyCount;

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

	@PendencyCount
	private String rg;

	@PendencyCount
	private String cpf;

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

	private Integer pendencies;

	public Profile() {
	}

	public Profile(String name, String rg, String cpf, Date birthday, String mobile, GenderType gender,
			TshirtType tshirt, Integer pendencies, Integer userId, String userEmail, Integer cityId, String cityName,
			Integer stateId, String stateName, String stateAbbreviation) {
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
	}

	public Profile(User user) {
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

	public Integer getPendencies() {
		return pendencies;
	}

	public void setPendencies(Integer pendencies) {
		this.pendencies = pendencies;
	}

}
