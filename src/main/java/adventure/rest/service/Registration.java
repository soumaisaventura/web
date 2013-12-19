package adventure.rest.service;

import static javax.persistence.EnumType.STRING;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Gender;
import adventure.validator.UniqueUserEmail;

@JSEntity
public class Registration {

	@NotEmpty
	private String fullName;

	@Email
	@NotEmpty
	@UniqueUserEmail
	@Column(unique = true)
	private String email;

	@NotEmpty
	private String password;

	@Past
	@NotNull
	private Date birthday;

	@NotNull
	@Enumerated(STRING)
	private Gender gender;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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
}
