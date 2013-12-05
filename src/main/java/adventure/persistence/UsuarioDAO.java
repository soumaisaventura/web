package adventure.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Usuario;
import br.gov.frameworkdemoiselle.template.JPACrud;

public class UsuarioDAO extends JPACrud<Usuario, Long> {

	private static final long serialVersionUID = 1L;

	public List<Usuario> findByEmail(String email) {
		String jpql = "from " + this.getBeanClass().getName() + " where email = :email";

		TypedQuery<Usuario> query = getEntityManager().createQuery(jpql, Usuario.class);
		query.setParameter("email", email);

		return query.getResultList();
	}
}
