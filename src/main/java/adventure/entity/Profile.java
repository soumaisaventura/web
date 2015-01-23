package adventure.entity;

import static adventure.util.Constants.CPF_SIZE;
import static adventure.util.Constants.ENUM_SIZE;
import static adventure.util.Constants.NAME_SIZE;
import static adventure.util.Constants.RG_SIZE;
import static adventure.util.Constants.TELEPHONE_SIZE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.util.PendencyCount;

@Entity
@Table(name = "PROFILE")
public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne
	@JoinColumn(name = "ID")
	@ForeignKey(name = "FK_PROFILE_USER")
	private User user;

	@NotEmpty
	@Column(name = "NAME")
	@Size(max = NAME_SIZE)
	@Index(name = "IDX_PROFILE_NAME")
	private String name;

	@PendencyCount
	@Length(max = RG_SIZE)
	@Column(name = "RG")
	private String rg;

	@PendencyCount
	@Length(max = CPF_SIZE)
	@Column(name = "CPF")
	@Index(name = "IDX_PROFILE_CPF")
	private String cpf;

	@Past
	@PendencyCount
	@Temporal(DATE)
	@Column(name = "BIRTHDAY")
	private Date birthday;

	@PendencyCount
	@Length(max = TELEPHONE_SIZE)
	@Column(name = "MOBILE")
	private String mobile;

	@NotNull
	@Enumerated(STRING)
	@Column(name = "GENDER", length = ENUM_SIZE)
	private GenderType gender;

	@PendencyCount
	@ManyToOne(optional = true)
	@JoinColumn(name = "CITY_ID")
	@ForeignKey(name = "FK_USER_CITY")
	@Index(name = "IDX_USER_CITY")
	private City city;

	@NotNull
	@Column(name = "PENDENCIES")
	private Integer pendencies;

	public Profile() {
	}

	public Profile(String name, String rg, String cpf, Date birthday, String mobile, GenderType gender, Integer pendencies,
			Integer userId, String userEmail, Integer cityId, String cityName, Integer stateId, String stateName,
			String stateAbbreviation) {
		setName(name);
		setRg(rg);
		setCpf(cpf);
		setBirthday(birthday);
		setMobile(mobile);
		setGender(gender);
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
