package adventure.persistence;

import static javax.persistence.TemporalType.DATE;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class RaceDAO extends JPACrud<Race, Long> {

	private static final long serialVersionUID = 1L;

	public Race loadJustId(Long id) throws Exception {
		String jpql = "select new Race(r.id) from Race r where r.id = :id ";
		TypedQuery<Race> query = getEntityManager().createQuery(jpql, Race.class);
		query.setParameter("id", id);

		Race result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public List<Race> findNext() throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	new " + FindNext.class.getName() + "(r.id, r.name, r.date, count(p)) ");
		jpql.append(" from ");
		jpql.append(" 	Period p right join p.race r ");
		jpql.append(" where ");
		jpql.append(" 	r.date >= :date");
		jpql.append(" group by ");
		jpql.append(" 	r.id, ");
		jpql.append(" 	r.name, ");
		jpql.append(" 	r.date ");
		jpql.append(" order by ");
		jpql.append(" 	r.date ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("date", new Date(), DATE);

		return query.getResultList();
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
