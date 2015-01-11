package adventure.security;

import java.security.Principal;

import adventure.entity.Account;
import adventure.entity.Gender;
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

	public static User parse(Account account) {
		Long id = account.getId();
		String email = account.getEmail();
		String name = account.getProfile().getName();
		Gender gender = account.getProfile().getGender();

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
