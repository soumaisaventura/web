package adventure.persistence;

import adventure.entity.Event;
import adventure.entity.EventAnalytic;
import adventure.entity.EventRegistrationStatusByDay;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.TypedQuery;
import java.util.List;

import static adventure.entity.RegistrationStatusType.CONFIRMED;
import static adventure.entity.RegistrationStatusType.PENDENT;

@Transactional
public class EventAnalyticDAO extends JPACrud<Race, Integer> {

	private static final long serialVersionUID = 1L;

	public static EventAnalyticDAO getInstance() {
		return Beans.getReference(EventAnalyticDAO.class);
	}

	public List<EventRegistrationStatusByDay> getEventRegistrationStatusByDay(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new EventRegistrationStatusByDay( ";
		jpql += "        e.id, ";
		jpql += "        e.name, ";
		jpql += "        er.date, ";
		jpql += "        er.pendentCount, ";
		jpql += "        er.confirmedCount, ";
		jpql += "        er.cancelledCount ";
		jpql += "        ) ";
		jpql += "   from EventRegistrationStatusByDay er ";
		jpql += "   join er.event e ";
		jpql += "  where e = :event ";
		jpql += "  order by ";
		jpql += "        er.date asc ";

		TypedQuery<EventRegistrationStatusByDay> query = getEntityManager().createQuery(jpql,
				EventRegistrationStatusByDay.class);
		query.setParameter("event", event);

		return query.getResultList();
	}

	public List<EventAnalytic> getRegistrationByCategory(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "        ca.name ";
		jpql += "        || ' ' || ra.name ";
		jpql += "        , ";
		jpql += "        (select count(_re) ";
		jpql += "           from Registration _re ";
		jpql += "          where _re.raceCategory = rc ";
		jpql += "            and _re.status in (:statusConfirmed, :statusPendent) ";
		jpql += "        ) as _count ";
		jpql += "        ) ";
		jpql += "   from RaceCategory rc ";
		jpql += "   join rc.category ca ";
		jpql += "   join rc.race ra ";
		jpql += "  where ra.event = :event ";
		jpql += "  order by ";
		jpql += "       ra.id, ";
		jpql += "        _count desc ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("statusConfirmed", CONFIRMED);
		query.setParameter("statusPendent", PENDENT);

		return query.getResultList();
	}

	public List<EventAnalytic> getRegistrationByRace(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "        ra.name, ";
		jpql += "        (select count(_re) ";
		jpql += "           from Registration _re ";
		jpql += "           join _re.raceCategory _rc ";
		jpql += "          where _rc.race = ra ";
		jpql += "            and _re.status in (:statusConfirmed, :statusPendent) ";
		jpql += "        ) as _count ";
		jpql += "        ) ";
		jpql += "   from Race ra ";
		jpql += "  where ra.event = :event ";
		jpql += "  order by ";
		jpql += "        _count desc ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("statusConfirmed", CONFIRMED);
		query.setParameter("statusPendent", PENDENT);

		return query.getResultList();
	}

	public List<EventAnalytic> getRegistrationByStatus(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "        re.status, ";
		jpql += "        count(re) ";
		jpql += "        ) ";
		jpql += "   from Registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		jpql += "  where ra.event = :event ";
		jpql += "  group by ";
		jpql += "        re.status ";
		jpql += "  order by ";
		jpql += "        count(re) desc ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);

		return query.getResultList();
	}

	public List<EventAnalytic> getRegistrationByCity(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "        ci.name || '/' || st.abbreviation, ";
		jpql += "        count(re) ";
		jpql += "        ) ";
		jpql += "   from UserRegistration tf ";
		jpql += "   join tf.registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra, ";
		jpql += "        Profile pr ";
		jpql += "   join pr.city ci ";
		jpql += "   join ci.state st ";
		jpql += "  where ra.event = :event ";
		jpql += "    and re.status in (:statusConfirmed, :statusPendent) ";
		jpql += "    and tf.user = pr.user ";
		jpql += "  group by ";
		jpql += "        ci.id, st.id ";
		jpql += "  order by ";
		jpql += "        count(re) desc, ci.name ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("statusConfirmed", CONFIRMED);
		query.setParameter("statusPendent", PENDENT);

		return query.getResultList();
	}

	public List<EventAnalytic> getAmountRaised(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "          'amount_raised', ";
		jpql += "           sum(cast(ur.racePrice as integer)) ";
		jpql += "        ) ";
		jpql += "   from UserRegistration ur ";
		jpql += "   join ur.registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.event ev ";
		jpql += "  where ev = :event ";
		jpql += "    and re.status = :status ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();
	}

	public List<EventAnalytic> getAmountDiscounted(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "          'amount_discounted', ";
		jpql += "           sum(cast(pe.price as integer)) - sum(cast(ur.racePrice as integer)) ";
		jpql += "        ) ";
		jpql += "   from UserRegistration ur ";
		jpql += "   join ur.registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.event ev ";
		jpql += "   join re.period pe ";
		jpql += "  where ev = :event ";
		jpql += "    and re.status = :status ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();
	}

	public List<EventAnalytic> getGenderQuantity(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "          case when pr.gender = 'FEMALE' then 'F' else 'M' end, ";
		jpql += "          count(ur)";
		jpql += "        ) ";
		jpql += "   from UserRegistration ur ";
		jpql += "   join ur.registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.event ev, ";
		jpql += "        Profile pr ";
		jpql += "  where ur.user = pr.user ";
		jpql += "    and ev = :event ";
		jpql += "    and re.status = :status ";
		jpql += "  group by pr.gender ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();
	}
	
	

	public List<EventAnalytic> gerRegistrationByAgeGroup(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "        case ";
		jpql += "          when extract(year from age(pr.birthday)) < 18 then '<18' ";
		jpql += "          when extract(year from age(pr.birthday)) < 25 then '<18-24' ";
		jpql += "          when extract(year from age(pr.birthday)) < 35 then '<25-34' ";
		jpql += "          when extract(year from age(pr.birthday)) < 45 then '<35-44' ";
		jpql += "          when extract(year from age(pr.birthday)) < 55 then '<45-54' ";
		jpql += "          else '>55' ";
		jpql += "        end as age_group";
		jpql += "        , ";
		jpql += "        count(pr) ";
		jpql += "        ) ";
		jpql += "   from UserRegistration ur ";
		jpql += "   join ur.registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.event ev, ";
		jpql += "        Profile pr ";
		jpql += "  where ur.user = pr.user ";
		jpql += "    and ev = :event ";
		jpql += "    and re.status = :status ";
		jpql += "  group by 1 ";
		jpql += "  order by 1 ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();

	}


	public List<EventAnalytic> getRegistrationByTshirt(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new adventure.entity.EventAnalytic( ";
		jpql += "        case when pr.tshirt is null then 'Indefinido' else pr.tshirt end, ";
		jpql += "        count(re) ";
		jpql += "        ) ";
		jpql += "   from UserRegistration tf ";
		jpql += "   join tf.registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra, ";
		jpql += "        Profile pr ";
		jpql += "  where ra.event = :event ";
		jpql += "    and re.status in (:statusConfirmed, :statusPendent) ";
		jpql += "    and tf.user = pr.user ";
		jpql += "  group by ";
		jpql += "        pr.tshirt ";
		jpql += "  order by ";
		jpql += "        count(re) desc ";

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql, EventAnalytic.class);
		query.setParameter("event", event);
		query.setParameter("statusConfirmed", CONFIRMED);
		query.setParameter("statusPendent", PENDENT);

		return query.getResultList();
	}
}
