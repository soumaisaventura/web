package adventure.persistence;

import static adventure.entity.RegistrationStatusType.CONFIRMED;
import static adventure.entity.RegistrationStatusType.PENDENT;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Event;
import adventure.entity.EventAnalytic;
import adventure.entity.EventRegistrationStatusByDay;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class EventAnalyticDAO extends JPACrud<Race, Integer> {

	private static final long serialVersionUID = 1L;

	public static EventAnalyticDAO getInstance() {
		return Beans.getReference(EventAnalyticDAO.class);
	}

	public List<EventRegistrationStatusByDay> getEventRegistrationStatusByDay(Event event) {
		StringBuffer jpql = new StringBuffer();

		jpql.append(" select ");
		jpql.append("    new EventRegistrationStatusByDay( ");
		jpql.append("        e.id, ");
		jpql.append("        e.name, ");
		jpql.append("        er.date, ");
		jpql.append("        er.pendentCount, ");
		jpql.append("        er.confirmedCount, ");
		jpql.append("        er.cancelledCount ");
		jpql.append("        ) ");
		jpql.append("   from EventRegistrationStatusByDay er ");
		jpql.append("   join er.event e ");
		jpql.append("  where e = :event ");
		jpql.append("  order by ");
		jpql.append("        er.date asc ");

		TypedQuery<EventRegistrationStatusByDay> query = getEntityManager().createQuery(jpql.toString(),
				EventRegistrationStatusByDay.class);
		query.setParameter("event", event);

		return query.getResultList();
	}

	public List<EventAnalytic> getRegistrationByCategory(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("        ca.name ");
		jpql.append("        || ' ' || ra.name ");
		jpql.append("        , ");
		jpql.append("        (select count(_re) ");
		jpql.append("           from Registration _re ");
		jpql.append("          where _re.raceCategory = rc ");
		jpql.append("            and _re.status in (:statusConfirmed, :statusPendent) ");
		jpql.append("        ) as _count ");
		jpql.append("        ) ");
		jpql.append("   from RaceCategory rc ");
		jpql.append("   join rc.category ca ");
		jpql.append("   join rc.race ra ");
		jpql.append("  where ra.event = :event ");
		jpql.append("  order by ");
		jpql.append("       ra.id, ");
		jpql.append("        _count desc ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("statusConfirmed", CONFIRMED);
		query.setParameter("statusPendent", PENDENT);

		return query.getResultList();
	}

	public List<EventAnalytic> getRegistrationByRace(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("        ra.name, ");
		jpql.append("        (select count(_re) ");
		jpql.append("           from Registration _re ");
		jpql.append("           join _re.raceCategory _rc ");
		jpql.append("          where _rc.race = ra ");
		jpql.append("            and _re.status in (:statusConfirmed, :statusPendent) ");
		jpql.append("        ) as _count ");
		jpql.append("        ) ");
		jpql.append("   from Race ra ");
		jpql.append("  where ra.event = :event ");
		jpql.append("  order by ");
		jpql.append("        _count desc ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("statusConfirmed", CONFIRMED);
		query.setParameter("statusPendent", PENDENT);

		return query.getResultList();
	}

	public List<EventAnalytic> getRegistrationByStatus(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("        re.status, ");
		jpql.append("        count(re) ");
		jpql.append("        ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("  where ra.event = :event ");
		jpql.append("  group by ");
		jpql.append("        re.status ");
		jpql.append("  order by ");
		jpql.append("        count(re) desc ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);

		return query.getResultList();
	}

	public List<EventAnalytic> getRegistrationByCity(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("        ci.name || '/' || st.abbreviation, ");
		jpql.append("        count(re) ");
		jpql.append("        ) ");
		jpql.append("   from UserRegistration tf ");
		jpql.append("   join tf.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra, ");
		jpql.append("        Profile pr ");
		jpql.append("   join pr.city ci ");
		jpql.append("   join ci.state st ");
		jpql.append("  where ra.event = :event ");
		jpql.append("    and re.status in (:statusConfirmed, :statusPendent) ");
		jpql.append("    and tf.user = pr.user ");
		jpql.append("  group by ");
		jpql.append("        ci.id, st.id ");
		jpql.append("  order by ");
		jpql.append("        count(re) desc, ci.name ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("statusConfirmed", CONFIRMED);
		query.setParameter("statusPendent", PENDENT);

		return query.getResultList();
	}

	public List<EventAnalytic> getAmountRaised(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("          'amount_raised', ");
		jpql.append("           sum(cast(ur.racePrice as integer)) ");
		jpql.append("        ) ");
		jpql.append("   from UserRegistration ur ");
		jpql.append("   join ur.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.event ev ");
		jpql.append("  where ev = :event ");
		jpql.append("    and re.status = :status ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();
	}

	public List<EventAnalytic> getAmountDiscounted(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("          'amount_discounted', ");
		jpql.append("           sum(cast(pe.price as integer)) - sum(cast(ur.racePrice as integer)) ");
		jpql.append("        ) ");
		jpql.append("   from UserRegistration ur ");
		jpql.append("   join ur.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.event ev ");
		jpql.append("   join re.period pe ");
		jpql.append("  where ev = :event ");
		jpql.append("    and re.status = :status ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();
	}

	public List<EventAnalytic> getGenderQuantity(Event event) {

		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("          case when pr.gender = 'FEMALE' then 'F' else 'M' end, ");
		jpql.append("          count(ur)");
		jpql.append("        ) ");
		jpql.append("   from UserRegistration ur ");
		jpql.append("   join ur.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.event ev, ");
		jpql.append("        Profile pr ");
		jpql.append("  where ur.user = pr.user ");
		jpql.append("    and ev = :event ");
		jpql.append("    and re.status = :status ");
		jpql.append("  group by pr.gender ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();
	}
	
	

	public List<EventAnalytic> gerRegistrationByAgeGroup(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("        case ");
		jpql.append("          when extract(year from age(pr.birthday)) < 18 then '<18' ");
		jpql.append("          when extract(year from age(pr.birthday)) < 25 then '<18-24' ");
		jpql.append("          when extract(year from age(pr.birthday)) < 35 then '<25-34' ");
		jpql.append("          when extract(year from age(pr.birthday)) < 45 then '<35-44' ");
		jpql.append("          when extract(year from age(pr.birthday)) < 55 then '<45-54' ");
		jpql.append("          else '>55' ");
		jpql.append("        end as age_group");
		jpql.append("        , ");
		jpql.append("        count(pr) ");
		jpql.append("        ) ");
		jpql.append("   from UserRegistration ur ");
		jpql.append("   join ur.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.event ev, ");
		jpql.append("        Profile pr ");
		jpql.append("  where ur.user = pr.user ");
		jpql.append("    and ev = :event ");
		jpql.append("    and re.status = :status ");
		jpql.append("  group by 1 ");
		jpql.append("  order by 1 ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();

	}


	public List<EventAnalytic> getRegistrationByTshirt(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("        case when pr.tshirt is null then 'Indefinido' else pr.tshirt end, ");
		jpql.append("        count(re) ");
		jpql.append("        ) ");
		jpql.append("   from UserRegistration tf ");
		jpql.append("   join tf.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra, ");
		jpql.append("        Profile pr ");
		jpql.append("  where ra.event = :event ");
		jpql.append("    and re.status in (:statusConfirmed, :statusPendent) ");
		jpql.append("    and tf.user = pr.user ");
		jpql.append("  group by ");
		jpql.append("        pr.tshirt ");
		jpql.append("  order by ");
		jpql.append("        count(re) desc ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("statusConfirmed", CONFIRMED);
		query.setParameter("statusPendent", PENDENT);

		return query.getResultList();
	}
}
