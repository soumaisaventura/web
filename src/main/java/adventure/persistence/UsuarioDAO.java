package adventure.persistence;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;

public class UsuarioDAO extends JPACrud<User, Long> {

	private static final long serialVersionUID = 1L;

	public User loadByEmail(String email) {
		String jpql = "from " + this.getBeanClass().getName() + " where email = :email";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
		query.setParameter("email", email);

		User result;

		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}
}
