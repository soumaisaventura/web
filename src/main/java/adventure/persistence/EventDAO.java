package adventure.persistence;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Event;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class EventDAO extends JPACrud<Event, Integer> {

	private static final long serialVersionUID = 1L;

	public static EventDAO getInstance() {
		return Beans.getReference(EventDAO.class);
	}

	public List<Event> findByYear(Integer year) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Event( ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.slug, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.description, ");
		jpql.append(" 	        e.site, ");
		jpql.append(" 	        c.id, ");
		jpql.append(" 	        c.name, ");
		jpql.append(" 	        s.id, ");
		jpql.append(" 	        s.name, ");
		jpql.append(" 	        s.abbreviation, ");
		jpql.append(" 	        e.coords.latitude, ");
		jpql.append(" 	        e.coords.longitude, ");
		jpql.append(" 	        e.beginning, ");
		jpql.append(" 	        e.end, ");
		jpql.append(" 	        e.status ");
		jpql.append(" 	     ) ");
		jpql.append("   from Event e ");
		jpql.append("   left join e.city c ");
		jpql.append("   left join c.state s ");
		jpql.append("  where e.id > 0 ");
		jpql.append("   and year(e.beginning) = :year ");
		jpql.append("  order by ");
		jpql.append("        e.beginning ");

		TypedQuery<Event> query = getEntityManager().createQuery(jpql.toString(), Event.class);
		query.setParameter("year", year);

		return query.getResultList();
	}

	public Event loadForMeta(String slug) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Event( ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.slug, ");
		jpql.append(" 	        e.description, ");
		jpql.append(" 	        e.beginning, ");
		jpql.append(" 	        e.end ");
		jpql.append(" 	     ) ");
		jpql.append("   from Event e ");
		jpql.append("  where e.slug = :slug ");

		TypedQuery<Event> query = getEntityManager().createQuery(jpql.toString(), Event.class);
		query.setParameter("slug", slug);

		Event result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public Event load(String slug) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select e ");
		jpql.append("   from Event e ");
		jpql.append("  where e.slug = :slug ");

		TypedQuery<Event> query = getEntityManager().createQuery(jpql.toString(), Event.class);
		query.setParameter("slug", slug);

		Event result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public Event loadForDetail(String slug) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Event( ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.slug, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.description, ");
		jpql.append(" 	        e.site, ");
		jpql.append(" 	        c.id, ");
		jpql.append(" 	        c.name, ");
		jpql.append(" 	        s.id, ");
		jpql.append(" 	        s.name, ");
		jpql.append(" 	        s.abbreviation, ");
		jpql.append(" 	        e.coords.latitude, ");
		jpql.append(" 	        e.coords.longitude, ");
		jpql.append(" 	        e.beginning, ");
		jpql.append(" 	        e.end, ");
		jpql.append(" 	        e.status ");
		jpql.append(" 	     ) ");
		jpql.append("   from Event e ");
		jpql.append("   left join e.city c ");
		jpql.append("   left join c.state s ");
		jpql.append("  where e.slug = :slug ");

		TypedQuery<Event> query = getEntityManager().createQuery(jpql.toString(), Event.class);
		query.setParameter("slug", slug);

		Event result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public Event loadForBanner(String slug) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Event( ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.banner ");
		jpql.append(" 	     ) ");
		jpql.append("   from Event e ");
		jpql.append("  where e.slug = :slug ");

		TypedQuery<Event> query = getEntityManager().createQuery(jpql.toString(), Event.class);
		query.setParameter("slug", slug);

		Event result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	// TODO: OLD
}
