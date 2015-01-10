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

		if (profile != null && profile.getAccount() != null && profile.getAccount().getPassword() != null) {
			String hash = Passwords.hash(credentials.getPassword(), credentials.getUsername());
			result = profile.getAccount().getPassword().equals(hash);
		}

		return result;
	}

	private boolean doesConfirmationTokenMatch(Profile profile) {
		boolean result = false;

		if (profile != null && profile.getAccount() != null && profile.getAccount().getConfirmationToken() != null) {
			ActivationSession session = Beans.getReference(ActivationSession.class);
			String hash = Passwords.hash(session.getToken(), profile.getAccount().getEmail());
			result = profile.getAccount().getConfirmationToken().equals(hash);
		}

		return result;
	}
}
