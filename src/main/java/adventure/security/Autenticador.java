package adventure.security;

import java.util.HashMap;
import java.util.Map;

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
		final Usuario usuario = dao.loadByEmail(credentials.getUsername());

		if (usuario != null && usuario.getSenha().equals(credentials.getPassword())) {
			this.user = new User() {

				private static final long serialVersionUID = 1L;

				private Map<Object, Object> attrs = new HashMap<Object, Object>();

				@Override
				public String getId() {
					return usuario.getEmail();
				}

				@Override
				public Object getAttribute(Object key) {
					return this.attrs.get(key);
				}

				@Override
				public void setAttribute(Object key, Object value) {
					this.attrs.put(key, value);
				}
			};
			
			this.user.setAttribute("id", usuario.getId());
		}
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
