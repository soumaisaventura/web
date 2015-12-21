package adventure.persistence;

import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.RegistrationPeriod;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class PeriodDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static PeriodDAO getInstance() {
		return Beans.getReference(PeriodDAO.class);
	}

	public List<RegistrationPeriod> findForEvent(Race race) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new RegistrationPeriod ( ");
		jpql.append("        p.beginning, ");
		jpql.append("        p.end, ");
		jpql.append("        p.price ");
		jpql.append("        ) ");
		jpql.append("   from RegistrationPeriod p ");
		jpql.append("  where p.race = :race ");
		jpql.append("  order by ");
		jpql.append("        p.beginning ");

		TypedQuery<RegistrationPeriod> query = em.createQuery(jpql.toString(), RegistrationPeriod.class);
		query.setParameter("race", race);

		return query.getResultList();
	}

	// TODO: OLD

	public RegistrationPeriod loadCurrent(Race race) throws Exception {
		return load(race, new Date());
	}

	public RegistrationPeriod load(Race race, Date date) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select p ");
		jpql.append("   from RegistrationPeriod p ");
		jpql.append("   join p.race r ");
		jpql.append("  where r = :race ");
		jpql.append(" 	 and :date between p.beginning and p.end ");
		jpql.append("  order by ");
		jpql.append("        p.beginning ");

		TypedQuery<RegistrationPeriod> query = em.createQuery(jpql.toString(), RegistrationPeriod.class);
		query.setParameter("race", race);
		query.setParameter("date", date, DATE);

		RegistrationPeriod result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public List<RegistrationPeriod> find(Race race) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select p ");
		jpql.append("   from RegistrationPeriod p ");
		jpql.append("   join p.race r ");
		jpql.append("  where r = :race ");
		jpql.append("  order by ");
		jpql.append("        p.beginning ");

		TypedQuery<RegistrationPeriod> query = em.createQuery(jpql.toString(), RegistrationPeriod.class);
		query.setParameter("race", race);

		return query.getResultList();
	}
}
