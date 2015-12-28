package adventure.persistence;

import static adventure.entity.EventPaymentType.AUTO;
import static adventure.entity.Status.CLOSED_ID;
import static adventure.entity.Status.OPEN_ID;

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

	public Race loadMetaOgg(String raceSlug, String eventSlug) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.slug, ");
		jpql.append(" 	        r.description, ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.slug, ");
		jpql.append(" 	        e.description ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		jpql.append("   join r.event e ");
		jpql.append("  where r.slug = :raceSlug ");
		jpql.append("    and e.slug = :eventSlug ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
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
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.slug, ");
		jpql.append(" 	        r.description, ");
		jpql.append(" 	        r.period.beginning, ");
		jpql.append(" 	        r.period.end, ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.slug, ");
		jpql.append(" 	        e.description, ");
		jpql.append(" 	        e.site, ");
		jpql.append(" 	        e.payment.account, ");
		jpql.append(" 	        e.payment.token, ");
		jpql.append(" 	        c.id, ");
		jpql.append(" 	        c.name, ");
		jpql.append(" 	        s.id, ");
		jpql.append(" 	        s.name, ");
		jpql.append(" 	        s.abbreviation, ");
		jpql.append(" 	        a.id, ");
		jpql.append(" 	        a.name ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		jpql.append("   join r.event e ");
		jpql.append("   join r.status a ");
		jpql.append("   join e.city c ");
		jpql.append("   join c.state s ");
		jpql.append("  where r.slug = :raceSlug ");
		jpql.append("    and e.slug = :eventSlug ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
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
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new Race( ");
		jpql.append(" 	        r.id, ");
		jpql.append(" 	        r.name, ");
		jpql.append(" 	        r.slug, ");
		jpql.append(" 	        r.description, ");
		jpql.append(" 	        r.period.beginning, ");
		jpql.append(" 	        r.period.end, ");
		jpql.append(" 	        e.id, ");
		jpql.append(" 	        e.name, ");
		jpql.append(" 	        e.slug, ");
		jpql.append(" 	        e.description, ");
		jpql.append(" 	        e.site, ");
		jpql.append(" 	        e.payment.account, ");
		jpql.append(" 	        e.payment.token, ");
		jpql.append(" 	        c.id, ");
		jpql.append(" 	        c.name, ");
		jpql.append(" 	        s.id, ");
		jpql.append(" 	        s.name, ");
		jpql.append(" 	        s.abbreviation, ");
		jpql.append(" 	        a.id, ");
		jpql.append(" 	        a.name ");
		jpql.append(" 	     ) ");
		jpql.append("   from Race r ");
		jpql.append("   join r.event e ");
		jpql.append("   join r.status a ");
		jpql.append("   join e.city c ");
		jpql.append("   join c.state s ");
		jpql.append("  where e.payment.type = :eventPaymentType ");
		jpql.append("    and a.id in (:raceOpenStatusId, :raceClosedStatusId) ");

		TypedQuery<Race> query = getEntityManager().createQuery(jpql.toString(), Race.class);
		query.setParameter("eventPaymentType", AUTO);
		query.setParameter("raceOpenStatusId", OPEN_ID);
		query.setParameter("raceClosedStatusId", CLOSED_ID);

		return query.getResultList();
	}
}
