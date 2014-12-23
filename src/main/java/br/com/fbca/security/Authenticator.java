package br.com.fbca.security;

import java.security.Principal;

import br.com.fbca.entity.User;
import br.com.fbca.persistence.UserDAO;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.InvalidCredentialsException;
import br.gov.frameworkdemoiselle.security.TokenAuthenticator;
import br.gov.frameworkdemoiselle.util.Beans;

public class Authenticator extends TokenAuthenticator {

	private static final long serialVersionUID = 1L;

	@Override
	protected Principal customAuthentication() throws Exception {
		Principal result = null;
		Credentials credentials = Beans.getReference(Credentials.class);
		User user = Beans.getReference(UserDAO.class).loadByEmail(credentials.getUsername());

		if (Beans.getReference(OAuthSession.class).isActive() || doesPasswordMatch(user, credentials)) {
			result = user;
		} else {
			throw new InvalidCredentialsException();
		}

		return result;
	}

	private boolean doesPasswordMatch(User user, Credentials credentials) {
		boolean result = false;

		if (user != null) {
			result = user.getPassword().equals(Passwords.hash(credentials.getPassword()));
		}

		return result;
	}
}
