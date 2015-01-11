package adventure.persistence;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import adventure.entity.Profile;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
// public class ProfileDAO extends JPACrud<Profile, Account> {
public class ProfileDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	// @Override
	public Profile insert(Profile entity) {
		if (entity.getName() != null) {
			entity.setName(entity.getName().trim());
		}

		em.persist(entity);
		return entity;
	}

	public Profile update(Profile entity) {
		em.merge(entity);
		return entity;
	}

	// @Override
	// public List<Profile> findAll() {
	// StringBuffer jpql = new StringBuffer();
	// jpql.append(" select ");
	// jpql.append(" 	new " + FindAll.class.getName() + "(a.id, a.email, p.name) ");
	// jpql.append(" from ");
	// jpql.append(" 	Profile p join p.account a ");
	//
	// TypedQuery<Profile> query = getEntityManager().createQuery(jpql.toString(), Profile.class);
	//
	// return query.getResultList();
	// }
	//
	// public Profile load(String email) {
	// String jpql = "from Profile where account.email = :email";
	// TypedQuery<Profile> query = getEntityManager().createQuery(jpql, Profile.class);
	// query.setParameter("email", email);
	//
	// Profile result;
	//
	// try {
	// result = query.getSingleResult();
	// } catch (NoResultException cause) {
	// result = null;
	// }
	//
	// return result;
	// }
	//
	// public Profile load(Long id) {
	// String jpql = "from Profile where account.id = :id";
	// TypedQuery<Profile> query = getEntityManager().createQuery(jpql, Profile.class);
	// query.setParameter("id", id);
	//
	// Profile result;
	//
	// try {
	// result = query.getSingleResult();
	// } catch (NoResultException cause) {
	// result = null;
	// }
	//
	// return result;
	// }
	//
	// @Override
	// public Profile load(Account account) {
	// String jpql = "from Profile where account = :account";
	// TypedQuery<Profile> query = getEntityManager().createQuery(jpql, Profile.class);
	// query.setParameter("account", account);
	//
	// Profile result;
	//
	// try {
	// result = query.getSingleResult();
	// } catch (NoResultException cause) {
	// result = null;
	// }
	//
	// return result;
	// }
	//
	// public static class FindAll extends Profile {
	//
	// private static final long serialVersionUID = 1L;
	//
	// public FindAll(Long id, String email, String name) {
	// setName(name);
	//
	// setAccount(new Account());
	// getAccount().setId(id);
	// getAccount().setEmail(email);
	// }
	// }
}
