package adventure.persistence;

import adventure.entity.Event;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class EventDAO extends JPACrud<Event, Integer> {

    private static final long serialVersionUID = 1L;

    public static EventDAO getInstance() {
        return Beans.getReference(EventDAO.class);
    }

    public List<Event> findByYear(Integer year) throws Exception {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Event( ";
        jpql += " 	     e.id, ";
        jpql += " 	     e.alias, ";
        jpql += " 	     e.name, ";
        jpql += " 	     e.description, ";
        jpql += " 	     e.site, ";
        jpql += "        c.id, ";
        jpql += "        c.name, ";
        jpql += "        s.id, ";
        jpql += "        s.name, ";
        jpql += "        s.abbreviation, ";
        jpql += "        y.id, ";
        jpql += "        y.name, ";
        jpql += "        y.abbreviation, ";
        jpql += " 	     e.beginning, ";
        jpql += " 	     e.end, ";
        jpql += " 	     e.status ";
        jpql += " 	     ) ";
        jpql += "   from Event e ";
        jpql += "   left join e.city c ";
        jpql += "   left join c.state s ";
        jpql += "   left join s.country y ";
        jpql += "  where year(e.beginning) = :year ";
        jpql += "    and e.show = true ";
        jpql += "  order by ";
        jpql += "        e.beginning, ";
        jpql += " 	     e.end, ";
        jpql += " 	     e.id ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
        query.setParameter("year", year);

        return query.getResultList();
    }

    public Event loadForMeta(String alias) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Event( ";
        jpql += " 	     e.id, ";
        jpql += " 	     e.name, ";
        jpql += " 	     e.alias, ";
        jpql += " 	     e.description, ";
        jpql += " 	     e.beginning, ";
        jpql += " 	     e.end ";
        jpql += " 	     ) ";
        jpql += "   from Event e ";
        jpql += "  where e.alias = :alias ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
        query.setParameter("alias", alias);

        Event result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    public Event load(String alias) {
        String jpql = "";
        jpql += " select e ";
        jpql += "   from Event e ";
        jpql += "  where e.alias = :alias ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
        query.setParameter("alias", alias);

        Event result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    public Event loadForDetail(String alias) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Event( ";
        jpql += " 	     e.id, ";
        jpql += " 	     e.alias, ";
        jpql += " 	     e.name, ";
        jpql += " 	     e.description, ";
        jpql += " 	     e.site, ";
        jpql += "        c.id, ";
        jpql += "        c.name, ";
        jpql += "        s.id, ";
        jpql += "        s.name, ";
        jpql += "        s.abbreviation, ";
        jpql += "        y.id, ";
        jpql += "        y.name, ";
        jpql += "        y.abbreviation, ";
        jpql += " 	     e.beginning, ";
        jpql += " 	     e.end, ";
        jpql += " 	     e.status ";
        jpql += " 	     ) ";
        jpql += "   from Event e ";
        jpql += "   left join e.city c ";
        jpql += "   left join c.state s ";
        jpql += "   left join s.country y ";
        jpql += "  where e.alias = :alias ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
        query.setParameter("alias", alias);

        Event result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    public Event loadForBanner(String alias) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Event( ";
        jpql += " 	     e.id, ";
        jpql += " 	     e.banner, ";
        jpql += " 	     e.bannerHash, ";
        jpql += " 	     e.status ";
        jpql += " 	     ) ";
        jpql += "   from Event e ";
        jpql += "  where e.alias = :alias ";

        TypedQuery<Event> query = getEntityManager().createQuery(jpql, Event.class);
        query.setParameter("alias", alias);

        Event result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }
}
