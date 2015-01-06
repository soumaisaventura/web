package adventure.security;

import java.security.Principal;

import adventure.entity.Profile;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;

public class User implements Principal {

	private Long id;

	private String email;

	private String name;

	public static User getLoggedIn() {
		return (User) Beans.getReference(SecurityContext.class).getUser();
	}

	public static User parse(Profile profile) {
		return new User(profile.getAccount().getId(), profile.getAccount().getEmail(), profile.getName());
	}

	private User(Long id, String email, String name) {
		this.id = id;
		this.email = email;
		this.name = name;
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
}
