package adventure.persistence;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.TeamFormation;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class TeamFormationDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public static TeamFormationDAO getInstance() {
		return Beans.getReference(TeamFormationDAO.class);
	}

	@Inject
	private EntityManager em;

	public TeamFormation insert(TeamFormation entity) {
		em.persist(entity);
		return entity;
	}

	public TeamFormation update(TeamFormation entity) {
		em.merge(entity);
		return entity;
	}

	public List<TeamFormation> find(Registration registration) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new TeamFormation( ");
		jpql.append(" 	     r.id, ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     pr.name, ");
		jpql.append(" 	     pr.gender, ");
		jpql.append(" 	     pr.mobile, ");
		jpql.append(" 	     tf.racePrice, ");
		jpql.append(" 	     tf.annualFee ");
		jpql.append(" 	     ) ");
		jpql.append("   from TeamFormation tf ");
		jpql.append("   join tf.user u ");
		jpql.append("   join tf.registration r, ");
		jpql.append("        Profile pr ");
		jpql.append("  where u = pr.user ");
		jpql.append("    and r = :registration ");
		jpql.append("  order by ");
		jpql.append("        pr.name ");

		TypedQuery<TeamFormation> query = em.createQuery(jpql.toString(), TeamFormation.class);
		query.setParameter("registration", registration);

		return query.getResultList();
	}

	public TeamFormation load(Registration registration, User user) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select tf ");
		jpql.append("   from TeamFormation tf ");
		jpql.append("  where tf.registration = :registration ");
		jpql.append("    and tf.user = :user ");

		TypedQuery<TeamFormation> query = em.createQuery(jpql.toString(), TeamFormation.class);
		query.setParameter("registration", registration);
		query.setParameter("user", user);

		TeamFormation result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public TeamFormation loadForRegistrationSubmissionValidation(Race race, User user) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new TeamFormation( ");
		jpql.append("        u.id, ");
		jpql.append("        r.id, ");
		jpql.append("        r.teamName ");
		jpql.append("        ) ");
		jpql.append("   from TeamFormation tf ");
		jpql.append("   join tf.user u ");
		jpql.append("   join tf.registration r ");
		jpql.append("   join r.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("  where ra = :race ");
		jpql.append("    and u = :user ");

		TypedQuery<TeamFormation> query = em.createQuery(jpql.toString(), TeamFormation.class);
		query.setParameter("race", race);
		query.setParameter("user", user);

		TeamFormation result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}
}
