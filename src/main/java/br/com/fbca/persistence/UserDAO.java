package br.com.fbca.persistence;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.fbca.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class UserDAO extends JPACrud<User, Long> {

	private static final long serialVersionUID = 1L;

	public User loadByEmail(String email) {
		String jpql = "from " + this.getBeanClass().getName() + " where email = :email";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
		query.setParameter("email", email);

		User result;

		try {
			result = query.getSingleResult();
			getEntityManager().detach(result);

		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}
}
