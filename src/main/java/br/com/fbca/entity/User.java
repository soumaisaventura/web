package br.com.fbca.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import java.security.Principal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

	public User() {
	}

	public User(Userinfo userInfo) {
		if (!userInfo.getVerifiedEmail()) {
			throw new IllegalStateException("O e-mail não foi verificado");
		}

		this.name = userInfo.getName();
		this.email = userInfo.getEmail();

		// if (userInfo.getGender() != null) {
		// this.gender = Gender.valueOf(userInfo.getGender().toUpperCase());
		// }
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
}
