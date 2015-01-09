package adventure.security;

import java.security.Principal;

import adventure.entity.Gender;
import adventure.entity.Profile;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;

public class User implements Principal {

	private Long id;

	private String email;

	private String name;

	private Gender gender;

	public static User getLoggedIn() {
		return (User) Beans.getReference(SecurityContext.class).getUser();
	}

	public static User parse(Profile profile) {
		Long id = profile.getAccount().getId();
		String email = profile.getAccount().getEmail();
		String name = profile.getName();
		Gender gender = profile.getGender();

		return new User(id, email, name, gender);
	}

	public User(Long id, String email, String name, Gender gender) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.gender = gender;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}
}
