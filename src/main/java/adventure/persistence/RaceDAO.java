package adventure.persistence;

import static javax.persistence.TemporalType.DATE;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class RaceDAO extends JPACrud<Race, Integer> {

	private static final long serialVersionUID = 1L;

	public static RaceDAO getInstance() {
		return Beans.getReference(RaceDAO.class);
	}

	public Race loadForDetail(Integer id) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.description, ");
		jpql.append(" 	        r.date, ");
		jpql.append(" 	        r.paymentAccount, ");
		jpql.append(" 	        r.paymentToken, ");
		jpql.append(" 	        c.id, ");
		jpql.append(" 	        c.name, ");
		jpql.append(" 	        s.id, ");
		jpql.append(" 	        s.name, ");
		jpql.append(" 	        s.abbreviation, ");
		jpql.append(" 	       (select count(p) ");
		jpql.append(" 	          from Period p ");
		jpql.append(" 	         where p.race = r ");
		jpql.append(" 	           and :date between p.beginning and p.end) ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		jpql.append("   left join r.city c ");
		jpql.append("   left join c.state s ");
		jpql.append("  where r.id = :id ");
		jpql.append("  order by ");
		jpql.append("        r.date ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("date", new Date(), DATE);
		query.setParameter("id", id);

		Race result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public Race loadJustId(Integer id) throws Exception {
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
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.description, ");
		jpql.append(" 	        r.date, ");
		jpql.append(" 	        r.paymentAccount, ");
		jpql.append(" 	        r.paymentToken, ");
		jpql.append(" 	        c.id, ");
		jpql.append(" 	        c.name, ");
		jpql.append(" 	        s.id, ");
		jpql.append(" 	        s.name, ");
		jpql.append(" 	        s.abbreviation, ");
		jpql.append(" 	       (select count(p) ");
		jpql.append(" 	          from Period p ");
		jpql.append(" 	         where p.race = r ");
		jpql.append(" 	           and :date between p.beginning and p.end) ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		jpql.append("   left join r.city c ");
		jpql.append("   left join c.state s ");
		jpql.append("  where r.date >= :date ");
		jpql.append("  order by ");
		jpql.append("        r.date ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("date", new Date(), DATE);

		return query.getResultList();
	}

	public List<Race> findOpen() throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.description, ");
		jpql.append(" 	        r.date, ");
		jpql.append(" 	        r.paymentAccount, ");
		jpql.append(" 	        r.paymentToken, ");
		jpql.append(" 	        c.id, ");
		jpql.append(" 	        c.name, ");
		jpql.append(" 	        s.id, ");
		jpql.append(" 	        s.name, ");
		jpql.append(" 	        s.abbreviation, ");
		jpql.append(" 	        count(r.id) ");
		jpql.append(" 	     ) ");
		jpql.append("   from Period p ");
		jpql.append("   join p.race r ");
		jpql.append("   left join r.city c ");
		jpql.append("   left join c.state s ");
		jpql.append("  where :date between p.beginning and p.end ");
		jpql.append("  group by ");
		jpql.append("        r.id, ");
		jpql.append("        c.id, ");
		jpql.append("        s.id ");
		jpql.append("  order by ");
		jpql.append("        r.date ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("date", new Date(), DATE);

		return query.getResultList();
	}
}
