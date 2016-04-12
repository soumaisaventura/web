package adventure.persistence;

import adventure.entity.Event;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

import static adventure.entity.EventPaymentType.AUTO;
import static adventure.entity.Status.CLOSED_ID;
import static adventure.entity.Status.OPEN_ID;

@Transactional
public class RaceDAO extends JPACrud<Race, Integer> {

    private static final long serialVersionUID = 1L;

    public static RaceDAO getInstance() {
        return Beans.getReference(RaceDAO.class);
    }

    public List<Race> findForEvent(Event event) {
        String jpql = "";
        jpql += " select new Race( ";
        jpql += " 	        r.id, ";
        jpql += " 	        r.alias, ";
        jpql += " 	        r.name, ";
        jpql += " 	        r.description, ";
        jpql += " 	        r.distance, ";
        jpql += " 	        o.id, ";
        jpql += " 	        o.name, ";
        jpql += " 	        o.alias, ";
        jpql += " 	        r.period.beginning, ";
        jpql += " 	        r.period.end, ";
        jpql += " 	        r.status ";
        jpql += " 	     ) ";
        jpql += "   from Race r ";
        jpql += "        join r.sport o ";
        jpql += "  where r.event = :event ";
        jpql += "  order by ";
        jpql += "        r.period.beginning, ";
        jpql += "        o.id, ";
        jpql += "        r.distance desc ";

        TypedQuery<Race> query = getEntityManager().createQuery(jpql, Race.class);
        query.setParameter("event", event);

        return query.getResultList();
    }

    public Race loadMetaOgg(String raceSlug, String eventSlug) {
        String jpql = "";
        jpql += " select new Race( ";
        jpql += " 	        r.id, ";
        jpql += " 	        r.name, ";
        jpql += " 	        r.alias, ";
        jpql += " 	        r.description, ";
        jpql += " 	        e.id, ";
        jpql += " 	        e.name, ";
        jpql += " 	        e.alias, ";
        jpql += " 	        e.description ";
        jpql += " 	     ) ";
        jpql += "   from Race r ";
        jpql += "   join r.event e ";
        jpql += "  where r.alias = :raceSlug ";
        jpql += "    and e.alias = :eventSlug ";

        TypedQuery<Race> query = getEntityManager().createQuery(jpql, Race.class);
        query.setParameter("raceSlug", raceSlug);
        query.setParameter("eventSlug", eventSlug);

        Race result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    public Race loadForDetail(String raceSlug, String eventSlug) throws Exception {
        String jpql = "";
        jpql += " select new Race( ";
        jpql += " 	        r.id, ";
        jpql += " 	        r.name, ";
        jpql += " 	        r.alias, ";
        jpql += " 	        r.description, ";
        jpql += " 	        r.period.beginning, ";
        jpql += " 	        r.period.end, ";
        jpql += " 	        e.id, ";
        jpql += " 	        e.name, ";
        jpql += " 	        e.alias, ";
        jpql += " 	        e.description, ";
        jpql += " 	        e.site, ";
        jpql += " 	        e.payment.account, ";
        jpql += " 	        e.payment.token, ";
        jpql += " 	        c.id, ";
        jpql += " 	        c.name, ";
        jpql += " 	        s.id, ";
        jpql += " 	        s.name, ";
        jpql += " 	        s.abbreviation, ";
        jpql += " 	        a.id, ";
        jpql += " 	        a.name ";
        jpql += " 	     ) ";
        jpql += "   from Race r ";
        jpql += "   join r.event e ";
        jpql += "   join r.status a ";
        jpql += "   join e.city c ";
        jpql += "   join c.state s ";
        jpql += "  where r.alias = :raceSlug ";
        jpql += "    and e.alias = :eventSlug ";

        TypedQuery<Race> query = getEntityManager().createQuery(jpql, Race.class);
        query.setParameter("raceSlug", raceSlug);
        query.setParameter("eventSlug", eventSlug);

        Race result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    public List<Race> findOpenAutoPayment() throws Exception {
        String jpql = "";
        jpql += " select new Race( ";
        jpql += " 	        r.id, ";
        jpql += " 	        r.name, ";
        jpql += " 	        r.alias, ";
        jpql += " 	        r.description, ";
        jpql += " 	        r.period.beginning, ";
        jpql += " 	        r.period.end, ";
        jpql += " 	        e.id, ";
        jpql += " 	        e.name, ";
        jpql += " 	        e.alias, ";
        jpql += " 	        e.description, ";
        jpql += " 	        e.site, ";
        jpql += " 	        e.payment.account, ";
        jpql += " 	        e.payment.token, ";
        jpql += " 	        c.id, ";
        jpql += " 	        c.name, ";
        jpql += " 	        s.id, ";
        jpql += " 	        s.name, ";
        jpql += " 	        s.abbreviation, ";
        jpql += " 	        a.id, ";
        jpql += " 	        a.name ";
        jpql += " 	     ) ";
        jpql += "   from Race r ";
        jpql += "   join r.event e ";
        jpql += "   join r.status a ";
        jpql += "   join e.city c ";
        jpql += "   join c.state s ";
        jpql += "  where e.payment.type = :eventPaymentType ";
        jpql += "    and a.id in (:raceOpenStatusId, :raceClosedStatusId) ";

        TypedQuery<Race> query = getEntityManager().createQuery(jpql, Race.class);
        query.setParameter("eventPaymentType", AUTO);
        query.setParameter("raceOpenStatusId", OPEN_ID);
        query.setParameter("raceClosedStatusId", CLOSED_ID);

        return query.getResultList();
    }
}
