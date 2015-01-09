package adventure.security;

import java.security.Principal;

import adventure.entity.Profile;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;

public class User implements Principal {

	private Long id;

	private String email;

	private String name;

	private Boolean unconfirmed;

	public static User getLoggedIn() {
		return (User) Beans.getReference(SecurityContext.class).getUser();
	}

	public static User parse(Profile profile) {
		Long id = profile.getAccount().getId();
		String email = profile.getAccount().getEmail();
		String name = profile.getName();
		Boolean unconfirmed = null;

		if (profile.getAccount().getConfirmation() == null) {
			unconfirmed = true;
		}

		return new User(id, email, name, unconfirmed);
	}

	private User(Long id, String email, String name, Boolean unconfirmed) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.unconfirmed = unconfirmed;
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

	public Boolean getUnconfirmed() {
		return unconfirmed;
	}
}
