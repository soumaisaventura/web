package adventure.persistence;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Event;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class EventDAO extends JPACrud<Event, Integer> {

	private static final long serialVersionUID = 1L;

	public static EventDAO getInstance() {
		return Beans.getReference(EventDAO.class);
	}

	public Event loadForMeta(String slug) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Event( ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.description ");
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

	public Event loadForDetail(String slug) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Event( ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.description, ");
		jpql.append(" 	        e.slug, ");
		jpql.append(" 	        e.site, ");
		jpql.append(" 	        e.layout.textColor, ");
		jpql.append(" 	        e.layout.backgroundColor, ");
		jpql.append(" 	        e.layout.buttonColor ");
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

	public List<User> findOrganizers(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new User ( ");
		jpql.append(" 	        o.id, ");
		jpql.append(" 	        case when eo.alternateEmail is null then o.email else eo.alternateEmail end, ");
		jpql.append(" 	        case when eo.alternateName is null then p.name else eo.alternateName end, ");
		jpql.append(" 	        p.gender, ");
		jpql.append(" 	        p.mobile ");
		jpql.append(" 	     ) ");
		jpql.append("   from EventOrganizer eo ");
		jpql.append("   join eo.organizer o, ");
		jpql.append("        Profile p ");
		jpql.append("  where o.id = p.id ");
		jpql.append("    and eo.event = :event ");
		jpql.append("  order by ");
		jpql.append("        p.name ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("event", event);

		return query.getResultList();
	}
}
