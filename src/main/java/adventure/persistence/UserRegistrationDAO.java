package adventure.persistence;

import static adventure.entity.RegistrationStatusType.CANCELLED;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.User;
import adventure.entity.UserRegistration;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class UserRegistrationDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public static UserRegistrationDAO getInstance() {
		return Beans.getReference(UserRegistrationDAO.class);
	}

	@Inject
	private EntityManager em;

	public UserRegistration insert(UserRegistration entity) {
		em.persist(entity);
		return entity;
	}

	public UserRegistration update(UserRegistration entity) {
		em.merge(entity);
		return entity;
	}

	public List<UserRegistration> find(Registration registration) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new UserRegistration( ");
		jpql.append(" 	     r.id, ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     pr.name, ");
		jpql.append(" 	     pr.gender, ");
		jpql.append(" 	     pr.mobile, ");
		jpql.append(" 	     tf.racePrice ");
		jpql.append(" 	     ) ");
		jpql.append("   from UserRegistration tf ");
		jpql.append("   join tf.user u ");
		jpql.append("   join tf.registration r, ");
		jpql.append("        Profile pr ");
		jpql.append("  where u = pr.user ");
		jpql.append("    and r = :registration ");
		jpql.append("  order by ");
		jpql.append("        pr.name ");

		TypedQuery<UserRegistration> query = em.createQuery(jpql.toString(), UserRegistration.class);
		query.setParameter("registration", registration);

		return query.getResultList();
	}

	public UserRegistration load(Registration registration, User user) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select tf ");
		jpql.append("   from UserRegistration tf ");
		jpql.append("  where tf.registration = :registration ");
		jpql.append("    and tf.user = :user ");

		TypedQuery<UserRegistration> query = em.createQuery(jpql.toString(), UserRegistration.class);
		query.setParameter("registration", registration);
		query.setParameter("user", user);

		UserRegistration result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public UserRegistration loadForRegistrationSubmissionValidation(Race race, User user) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new UserRegistration( ");
		jpql.append("        u.id, ");
		jpql.append("        r.id, ");
		jpql.append("        r.teamName ");
		jpql.append("        ) ");
		jpql.append("   from UserRegistration tf ");
		jpql.append("   join tf.user u ");
		jpql.append("   join tf.registration r ");
		jpql.append("   join r.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("  where ra = :race ");
		jpql.append("    and u = :user ");
		jpql.append("    and r.status <> :status ");

		TypedQuery<UserRegistration> query = em.createQuery(jpql.toString(), UserRegistration.class);
		query.setParameter("race", race);
		query.setParameter("user", user);
		query.setParameter("status", CANCELLED);

		UserRegistration result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}
}
