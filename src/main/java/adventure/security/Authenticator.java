package adventure.security;

import java.security.Principal;

import javax.enterprise.context.RequestScoped;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.InvalidCredentialsException;
import br.gov.frameworkdemoiselle.util.Beans;

@RequestScoped
public class Authenticator implements br.gov.frameworkdemoiselle.security.Authenticator {

	private static final long serialVersionUID = 1L;

	private Principal user;

	@Override
	public void authenticate() throws Exception {
		Credentials credentials = Beans.getReference(Credentials.class);
		UserDAO dao = Beans.getReference(UserDAO.class);
		User usuario = dao.loadByEmail(credentials.getUsername());

		if (Beans.getReference(OAuthSession.class).isActive() || doesPasswordMatch(usuario, credentials)) {
			this.user = usuario;
		} else {
			throw new InvalidCredentialsException("usuário ou senha inválidos");
		}
	}

	private boolean doesPasswordMatch(User usuario, Credentials credentials) {
		boolean result = false;

		if (usuario != null) {
			result = usuario.getPassword().equals(Passwords.hash(credentials.getPassword()));
		}

		return result;
	}

	@Override
	public void unauthenticate() throws Exception {
		this.user = null;
	}

	@Override
	public Principal getUser() {
		return this.user;
	}
}
