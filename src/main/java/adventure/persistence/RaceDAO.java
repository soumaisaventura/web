package adventure.persistence;

import static adventure.entity.EventPaymentType.AUTO;
import static javax.persistence.TemporalType.DATE;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Event;
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

	public List<Race> findForEvent(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.slug, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.description, ");
		jpql.append(" 	        r.distance, ");
		jpql.append(" 	        o.id, ");
		jpql.append(" 	        o.name, ");
		jpql.append(" 	        o.acronym, ");
		jpql.append(" 	        r.period.beginning, ");
		jpql.append(" 	        r.period.end, ");
		jpql.append(" 	        r.status ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		jpql.append("        join r.sport o ");
		jpql.append("  where r.event = :event ");
		jpql.append("  order by ");
		jpql.append("        r.period.beginning, ");
		jpql.append("        o.id, ");
		jpql.append("        r.distance desc ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("event", event);

		return query.getResultList();
	}

	// TODO: OLD

	public Race loadForDetail(Integer id) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.description, ");
		// jpql.append(" 	        r.date, ");
		// jpql.append(" 	        r.site, ");
		// jpql.append(" 	        r.paymentAccount, ");
		// jpql.append(" 	        r.paymentToken, ");
		// jpql.append(" 	        c.id, ");
		// jpql.append(" 	        c.name, ");
		// jpql.append(" 	        s.id, ");
		// jpql.append(" 	        s.name, ");
		// jpql.append(" 	        s.abbreviation, ");
		// jpql.append(" 	       (select min(_p.beginning) ");
		// jpql.append(" 	          from Period _p ");
		// jpql.append(" 	         where _p.race = r), ");
		// jpql.append(" 	       (select max(_p.end) ");
		// jpql.append(" 	          from Period _p ");
		// jpql.append(" 	         where _p.race = r), ");
		jpql.append(" 	        r.status ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		// jpql.append("   left join r.city c ");
		// jpql.append("   left join c.state s ");
		jpql.append("  where r.id = :id ");
		// jpql.append("  order by ");
		// jpql.append("        r.date ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
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
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race(r.id) ");
		jpql.append("   from Race r ");
		jpql.append("  where r.id = :id ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
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
		// jpql.append(" 	        r.date, ");
		// jpql.append(" 	        r.site, ");
		// jpql.append(" 	        r.paymentAccount, ");
		// jpql.append(" 	        r.paymentToken, ");
		// jpql.append(" 	        c.id, ");
		// jpql.append(" 	        c.name, ");
		// jpql.append(" 	        s.id, ");
		// jpql.append(" 	        s.name, ");
		// jpql.append(" 	        s.abbreviation, ");
		// jpql.append(" 	       (select min(_p.beginning) ");
		// jpql.append(" 	          from Period _p ");
		// jpql.append(" 	         where _p.race = r), ");
		// jpql.append(" 	       (select max(_p.end) ");
		// jpql.append(" 	          from Period _p ");
		// jpql.append(" 	         where _p.race = r), ");
		jpql.append(" 	        r.status ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		// jpql.append("   left join r.city c ");
		// jpql.append("   left join c.state s ");
		jpql.append("  where r.beginning >= :date ");
		jpql.append("  order by ");
		jpql.append("        r.beginning ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("date", new Date(), DATE);

		return query.getResultList();
	}

	public List<Race> findByYear(Integer year) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.description, ");
		// jpql.append(" 	        r.date, ");
		// jpql.append(" 	        r.site, ");
		// jpql.append(" 	        r.paymentAccount, ");
		// jpql.append(" 	        r.paymentToken, ");
		// jpql.append(" 	        c.id, ");
		// jpql.append(" 	        c.name, ");
		// jpql.append(" 	        s.id, ");
		// jpql.append(" 	        s.name, ");
		// jpql.append(" 	        s.abbreviation, ");
		// jpql.append(" 	       (select min(_p.beginning) ");
		// jpql.append(" 	          from Period _p ");
		// jpql.append(" 	         where _p.race = r), ");
		// jpql.append(" 	       (select max(_p.end) ");
		// jpql.append(" 	          from Period _p ");
		// jpql.append(" 	         where _p.race = r), ");
		jpql.append(" 	        r.status ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		// jpql.append("   left join r.city c ");
		// jpql.append("   left join c.state s ");
		jpql.append("  where year(r.beginning) = :year ");
		jpql.append("    and r.id < 12 ");
		jpql.append("  order by ");
		jpql.append("        r.beginning ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("year", year);

		return query.getResultList();
	}

	public List<Race> findOpenAutoPayment() throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.description, ");
		// jpql.append(" 	        r.date, ");
		// jpql.append(" 	        r.site, ");
		// jpql.append(" 	        r.paymentAccount, ");
		// jpql.append(" 	        r.paymentToken, ");
		// jpql.append(" 	        c.id, ");
		// jpql.append(" 	        c.name, ");
		// jpql.append(" 	        s.id, ");
		// jpql.append(" 	        s.name, ");
		// jpql.append(" 	        s.abbreviation, ");
		// jpql.append(" 	       (select min(_p.beginning) ");
		// jpql.append(" 	          from Period _p ");
		// jpql.append(" 	         where _p.race = r), ");
		// jpql.append(" 	       (select max(_p.end) ");
		// jpql.append(" 	          from Period _p ");
		// jpql.append(" 	         where _p.race = r), ");
		jpql.append(" 	        r.status ");
		jpql.append(" 	     ) ");
		jpql.append("   from Period p ");
		jpql.append("   join p.race r ");
		// jpql.append("   left join r.city c ");
		// jpql.append("   left join c.state s ");
		jpql.append("  where :date between p.beginning and p.end ");
		jpql.append("    and r.paymentType = :paymentType ");
		jpql.append("  group by ");
		jpql.append("        r.id ");
		// jpql.append("        c.id, ");
		// jpql.append("        s.id ");
		jpql.append("  order by ");
		jpql.append("        r.beginning ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("date", new Date(), DATE);
		query.setParameter("paymentType", AUTO);

		return query.getResultList();
	}
}
