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
        String jpql = "";
        jpql += " select new Event( ";
        jpql += " 	        e.id, ";
        jpql += " 	        e.slug, ";
        jpql += " 	        e.name, ";
        jpql += " 	        e.description, ";
        jpql += " 	        e.site, ";
        jpql += " 	        c.id, ";
        jpql += " 	        c.name, ";
        jpql += " 	        s.id, ";
        jpql += " 	        s.name, ";
        jpql += " 	        s.abbreviation, ";
        jpql += " 	        e.beginning, ";
        jpql += " 	        e.end, ";
        jpql += " 	        e.status ";
        jpql += " 	     ) ";
        jpql += "   from Event e ";
        jpql += "   left join e.city c ";
        jpql += "   left join c.state s ";
        jpql += "  where year(e.beginning) = :year ";
        jpql += "    and e.id > 0 ";
        jpql += "  order by ";
        jpql += "        e.beginning ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
        query.setParameter("year", year);

        return query.getResultList();
    }

    public Event loadForMeta(String slug) {
        String jpql = "";
        jpql += " select new Event( ";
        jpql += " 	        e.id, ";
        jpql += " 	        e.name, ";
        jpql += " 	        e.slug, ";
        jpql += " 	        e.description, ";
        jpql += " 	        e.beginning, ";
        jpql += " 	        e.end ";
        jpql += " 	     ) ";
        jpql += "   from Event e ";
        jpql += "  where e.slug = :slug ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
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
        String jpql = "";
        jpql += " select e ";
        jpql += "   from Event e ";
        jpql += "  where e.slug = :slug ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
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
        String jpql = "";
        jpql += " select new Event( ";
        jpql += " 	        e.id, ";
        jpql += " 	        e.slug, ";
        jpql += " 	        e.name, ";
        jpql += " 	        e.description, ";
        jpql += " 	        e.site, ";
        jpql += " 	        c.id, ";
        jpql += " 	        c.name, ";
        jpql += " 	        s.id, ";
        jpql += " 	        s.name, ";
        jpql += " 	        s.abbreviation, ";
        jpql += " 	        e.beginning, ";
        jpql += " 	        e.end, ";
        jpql += " 	        e.status ";
        jpql += " 	     ) ";
        jpql += "   from Event e ";
        jpql += "   left join e.city c ";
        jpql += "   left join c.state s ";
        jpql += "  where e.slug = :slug ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
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
        String jpql = "";
        jpql += " select new Event( ";
        jpql += " 	        e.id, ";
        jpql += " 	        e.banner ";
        jpql += " 	     ) ";
        jpql += "   from Event e ";
        jpql += "  where e.slug = :slug ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
        query.setParameter("slug", slug);

        Event result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }
}
