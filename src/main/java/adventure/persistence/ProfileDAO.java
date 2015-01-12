package adventure.persistence;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Account;
import adventure.entity.Profile;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class ProfileDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

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

	public Profile load(Account id) {
		String jpql = "from Profile p where p.account = :account";
		TypedQuery<Profile> query = em.createQuery(jpql, Profile.class);
		query.setParameter("account", id);

		Profile result;

		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}
}
