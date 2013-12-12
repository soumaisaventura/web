package adventure.security;

import javax.enterprise.context.SessionScoped;

import adventure.entity.Usuario;
import adventure.persistence.UsuarioDAO;
import br.gov.frameworkdemoiselle.security.Authenticator;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.User;
import br.gov.frameworkdemoiselle.util.Beans;

@SessionScoped
public class Autenticador implements Authenticator {

	private static final long serialVersionUID = 1L;

	private User user;

	@Override
	public void authenticate() throws Exception {
		Credentials credentials = Beans.getReference(Credentials.class);
		UsuarioDAO dao = Beans.getReference(UsuarioDAO.class);
		Usuario usuario = dao.loadByEmail(credentials.getUsername());

		if (doesPasswordMatch(usuario, credentials)) {
			this.user = usuario.parse();
		}
	}

	private boolean doesPasswordMatch(Usuario usuario, Credentials credentials) {
		boolean result = false;

		if (usuario != null) {
			result = Hasher.digest(usuario.getSenha()).equals(credentials.getPassword());
		}

		return result;
	}

	@Override
	public void unauthenticate() throws Exception {
		this.user = null;
	}

	@Override
	public User getUser() {
		return this.user;
	}
}
