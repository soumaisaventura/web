package adventure.security;

import java.security.Principal;

import adventure.entity.User;
import adventure.persistence.UserDAO;
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
		User user = Beans.getReference(UserDAO.class).loadForAuthentication(credentials.getUsername());

		if (user != null && (isOAuthLogin() || isUserLogin(user, credentials))) {
			result = user;
		} else {
			throw new InvalidCredentialsException();
		}

		return result;
	}

	private boolean isOAuthLogin() {
		return Beans.getReference(OAuthSession.class).isActive();
	}

	private boolean isUserLogin(User user, Credentials credentials) {
		return doesConfirmationTokenMatch(user) || doesPasswordMatch(user, credentials);
	}

	private boolean doesConfirmationTokenMatch(User user) {
		boolean result = false;

		if (user.getConfirmationToken() != null) {
			ActivationSession session = Beans.getReference(ActivationSession.class);
			String hash = Passwords.hash(session.getToken(), user.getEmail());
			result = user.getConfirmationToken().equals(hash);
		}

		return result;
	}

	private boolean doesPasswordMatch(User user, Credentials credentials) {
		boolean result = false;

		if (user.getPassword() == null) {
			throw new PasswordNotDefinedException();

		} else if (user.getConfirmation() == null) {
			throw new UnconfirmedUserException();

		} else {
			String hash = Passwords.hash(credentials.getPassword(), credentials.getUsername());
			result = user.getPassword().equals(hash);
		}

		return result;
	}
}
