package adventure.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

import java.security.Principal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.api.services.oauth2.model.Userinfo;

@Entity
public class User implements Principal {

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	private String name;

	@Email
	@NotEmpty
	@Column(unique = true)
	private String email;

	private String password;

	@Past
	private Date birthday;

	@Enumerated(STRING)
	private Gender gender;

	private String rg;

	private String cpf;

	@Enumerated(STRING)
	private BloodType bloodType;

	public User() {
	}

	public User(Userinfo userInfo) {
		if (!userInfo.getVerifiedEmail()) {
			throw new IllegalStateException("O e-mail não foi verificado");
		}

		this.name = userInfo.getName();
		this.email = userInfo.getEmail();

		if (userInfo.getGender() != null) {
			this.gender = Gender.valueOf(userInfo.getGender().toUpperCase());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
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

	public BloodType getBloodType() {
		return bloodType;
	}

	public void setBloodType(BloodType bloodType) {
		this.bloodType = bloodType;
	}
}
