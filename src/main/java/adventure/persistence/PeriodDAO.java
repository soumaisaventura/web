package adventure.persistence;

import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Period;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class PeriodDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public Period loadCurrent(Race race) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select p ");
		jpql.append("   from Period p ");
		jpql.append("   join p.race r ");
		jpql.append("  where r = :race");
		jpql.append(" 	 and :date between p.beginning and p.end");

		TypedQuery<Period> query = em.createQuery(jpql.toString(), Period.class);
		query.setParameter("race", race);
		query.setParameter("date", new Date(), DATE);

		Period result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public static class FindNext extends Race {

		private static final long serialVersionUID = 1L;

		public FindNext(Long id, String name, Date date, Long count) {
			setId(id);
			setName(name);
			setDate(date);
			setOpen(count > 1);
		}
	}
}
