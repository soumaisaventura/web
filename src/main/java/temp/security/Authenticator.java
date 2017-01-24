package temp.security;

import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.InvalidCredentialsException;
import br.gov.frameworkdemoiselle.security.TokenAuthenticator;
import br.gov.frameworkdemoiselle.util.Beans;
import core.entity.User;
import core.persistence.UserDAO;

import java.security.Principal;

public class Authenticator extends TokenAuthenticator {

    private static final long serialVersionUID = 1L;

    @Override
    protected Principal customAuthentication() throws Exception {
        Principal result;
        Credentials credentials = Beans.getReference(Credentials.class);
        User user = UserDAO.getInstance().loadForAuthentication(credentials.getUsername());

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

        if (user.getActivationToken() != null) {
            ActivationSession session = Beans.getReference(ActivationSession.class);
            result = user.getActivationToken().equals(session.getToken());
        }

        return result;
    }

    private boolean doesPasswordMatch(User user, Credentials credentials) {
        boolean result;

        if (user.getPassword() == null) {
            throw new PasswordNotDefinedException();

        } else if (user.getActivation() == null) {
            throw new UnconfirmedUserException();

        } else {
            String hash = Passwords.hash(credentials.getPassword(), credentials.getUsername());
            result = user.getPassword().equals(hash);
        }

        return result;
    }
}
