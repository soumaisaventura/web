package adventure.persistence;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import adventure.entity.Health;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
// public class HealthDAO extends JPACrud<Health, Account> {
public class HealthDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public Health insert(Health entity) {
		em.persist(entity);
		return entity;
	}

	public Health update(Health entity) {
		em.merge(entity);
		return entity;
	}

	// public Health load(String email) {
	// String jpql = "from Health where account.email = :email";
	// TypedQuery<Health> query = getEntityManager().createQuery(jpql, Health.class);
	// query.setParameter("email", email);
	//
	// Health result;
	//
	// try {
	// result = query.getSingleResult();
	// } catch (EntityNotFoundException cause) {
	// result = null;
	// }
	//
	// return result;
	// }
	//
	// @Override
	// public Health load(Account account) {
	// String jpql = "from Health where account = :account";
	// TypedQuery<Health> query = getEntityManager().createQuery(jpql, Health.class);
	// query.setParameter("account", account);
	//
	// Health result;
	//
	// try {
	// result = query.getSingleResult();
	// } catch (EntityNotFoundException cause) {
	// result = null;
	// }
	//
	// return result;
	// }
}
