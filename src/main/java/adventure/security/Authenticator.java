package adventure.security;

import java.security.Principal;

import adventure.entity.Profile;
import adventure.persistence.ProfileDAO;
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
		OAuthSession oAuthSession = Beans.getReference(OAuthSession.class);
		Profile profile = Beans.getReference(ProfileDAO.class).load(credentials.getUsername());

		if (oAuthSession.isActive() || doesPasswordMatch(profile, credentials) || doesConfirmationTokenMatch(profile)) {
			result = User.parse(profile);
		} else {
			throw new InvalidCredentialsException();
		}

		return result;
	}

	private boolean doesPasswordMatch(Profile profile, Credentials credentials) {
		boolean result = false;

		if (profile.getAccount() != null && profile.getAccount().getPassword() != null) {
			String hash = Passwords.hash(credentials.getPassword());
			result = profile.getAccount().getPassword().equals(hash);
		}

		return result;
	}

	private boolean doesConfirmationTokenMatch(Profile profile) {
		ConfirmationTokenSession session = Beans.getReference(ConfirmationTokenSession.class);
		return profile.getAccount() != null && !session.isEmpty()
				&& session.getValue().equals(profile.getAccount().getConfirmationToken());
	}
}
