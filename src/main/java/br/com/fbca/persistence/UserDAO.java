package br.com.fbca.persistence;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.fbca.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class UserDAO extends JPACrud<User, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public User insert(User user) {
		user.setCreation(new Date());
		return super.insert(user);
	}

	@Override
	public void delete(Long id) {
		User user = load(id);

		if (user != null) {
			user.setDeleted(new Date());
			update(user);
		}
	}

	public User loadByEmail(String email, boolean includeDeleted) {
		String jpql = "from " + this.getBeanClass().getName() + " where email = :email";
		
		if (!includeDeleted) {
			jpql += " and deleted is null";
		}

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
	
	public User loadByEmail(String email) {
		return loadByEmail(email, false);
	}
}
