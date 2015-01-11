package adventure.security;

import java.security.Principal;

import adventure.entity.Account;
import adventure.persistence.AccountDAO;
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
		Account account = Beans.getReference(AccountDAO.class).loadForAuthentication(credentials.getUsername());

		if (account != null && (isOAuthLogin() || isAccountLogin(account, credentials))) {
			result = User.parse(account);
		} else {
			throw new InvalidCredentialsException();
		}

		return result;
	}

	private boolean isOAuthLogin() {
		return Beans.getReference(OAuthSession.class).isActive();
	}

	private boolean isAccountLogin(Account account, Credentials credentials) {
		return doesConfirmationTokenMatch(account) || doesPasswordMatch(account, credentials);
	}

	private boolean doesConfirmationTokenMatch(Account account) {
		boolean result = false;

		if (account.getConfirmationToken() != null) {
			ActivationSession session = Beans.getReference(ActivationSession.class);
			String hash = Passwords.hash(session.getToken(), account.getEmail());
			result = account.getConfirmationToken().equals(hash);
		}

		return result;
	}

	private boolean doesPasswordMatch(Account account, Credentials credentials) {
		boolean result = false;

		if (account.getPassword() == null) {
			throw new PasswordNotDefinedException();

		} else if (account.getConfirmation() == null) {
			throw new UnconfirmedAccountException();

		} else {
			String hash = Passwords.hash(credentials.getPassword(), credentials.getUsername());
			result = account.getPassword().equals(hash);
		}

		return result;
	}
}
