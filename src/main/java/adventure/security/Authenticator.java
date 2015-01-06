package adventure.security;

import java.security.Principal;

import adventure.entity.Account;
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
		Profile profile = Beans.getReference(ProfileDAO.class).load(credentials.getUsername());

		if (Beans.getReference(OAuthSession.class).isActive() || doesPasswordMatch(profile.getAccount(), credentials)) {
			result = User.parse(profile);
		} else {
			throw new InvalidCredentialsException();
		}

		return result;
	}

	private boolean doesPasswordMatch(Account account, Credentials credentials) {
		boolean result = false;

		if (account != null && account.getPassword() != null) {
			result = Passwords.hash(credentials.getPassword()).equals(account.getPassword());
		}

		return result;
	}
}
