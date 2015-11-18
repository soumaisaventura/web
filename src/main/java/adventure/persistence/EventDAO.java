package adventure.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Event;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class EventDAO extends JPACrud<Event, Integer> {

	private static final long serialVersionUID = 1L;

	public static EventDAO getInstance() {
		return Beans.getReference(EventDAO.class);
	}

	public List<Event> mapData() {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name2, ");
		jpql.append(" 	        r.slug, ");
		jpql.append(" 	        o.id, ");
		jpql.append(" 	        o.name, ");
		jpql.append(" 	        o.acronym, ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.slug, ");
		jpql.append(" 	        r.beginning, ");
		jpql.append(" 	        r.end, ");
		jpql.append(" 	        r.coords.latitude, ");
		jpql.append(" 	        r.coords.longitude ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		jpql.append("   join r.sport o ");
		jpql.append("   join r.event e ");
		jpql.append("  where e.id > 0 ");
		// jpql.append("     or :end between r.beginning and r.end ");
		jpql.append("  order by ");
		jpql.append("        r.beginning, ");
		jpql.append("        r.end ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		// query.setParameter("beginning", beginning, DATE);
		// query.setParameter("end", beginning, DATE);

		Map<Integer, Event> saved = new HashMap<Integer, Event>();
		List<Event> result = new ArrayList<Event>();
		for (Race race : query.getResultList()) {
			Event currentEvent = race.getEvent();
			race.setEvent(null);
			Event savedEvent = null;

			if (!saved.isEmpty()) {
				savedEvent = saved.get(currentEvent.getId());
			}

			if (savedEvent == null) {
				currentEvent.setRaces(new ArrayList<Race>());
				saved.put(currentEvent.getId(), currentEvent);
				savedEvent = currentEvent;
				result.add(currentEvent);
			}

			savedEvent.getRaces().add(race);
		}

		return result;
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

	// TODO: OLD
}
