package adventure.persistence;

import static adventure.entity.RegistrationStatusType.CONFIRMED;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Event;
import adventure.entity.Race;
import adventure.entity.EventAnalytic;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class EventAnalyticDAO extends JPACrud<Race, Integer> {

	private static final long serialVersionUID = 1L;

	public static EventAnalyticDAO getInstance() {
		return Beans.getReference(EventAnalyticDAO.class);
	}

	public List<EventAnalytic> getRegistrationByCategory(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("        ca.name ");
		// jpql.append("        || ' ' || co.name ");
		jpql.append("        , ");
		jpql.append("        (select count(_re) ");
		jpql.append("           from Registration _re ");
		jpql.append("          where _re.raceCategory = rc ");
		jpql.append("            and _re.status = :status ");
		jpql.append("        ) as _count ");
		jpql.append("        ) ");
		jpql.append("   from RaceCategory rc ");
		jpql.append("   join rc.category ca ");
		// jpql.append("   join rc.course co ");
		jpql.append("  where rc.race = :race ");
		jpql.append("  order by ");
		// jpql.append("        co.id, ");
		jpql.append("        _count desc ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("race", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();
	}

	// public List<EventAnalytic> getRegistrationByCourse(Race race) {
	// StringBuffer jpql = new StringBuffer();
	// jpql.append(" select ");
	// jpql.append("    new adventure.entity.EventAnalytic( ");
	// jpql.append("        co.name, ");
	// jpql.append("        (select count(_re) ");
	// jpql.append("           from Registration _re ");
	// jpql.append("           join _re.raceCategory _rc ");
	// jpql.append("          where _rc.course = co ");
	// jpql.append("            and _re.status = :status ");
	// jpql.append("        ) as _count ");
	// jpql.append("        ) ");
	// jpql.append("   from Course co ");
	// jpql.append("  where co.race = :race ");
	// jpql.append("  order by ");
	// jpql.append("        _count desc ");
	//
	// TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
	// query.setParameter("race", race);
	// query.setParameter("status", CONFIRMED);
	//
	// return query.getResultList();
	// }

	public List<EventAnalytic> getRegistrationByStatus(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new adventure.entity.EventAnalytic( ");
		jpql.append("        re.status, ");
		jpql.append("        count(re) ");
		jpql.append("        ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("  where rc.race = :race ");
		jpql.append("  group by ");
		jpql.append("        re.status ");
		jpql.append("  order by ");
		jpql.append("        count(re) desc ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("race", event);

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
		jpql.append("   join re.raceCategory rc, ");
		jpql.append("        Profile pr ");
		jpql.append("   join pr.city ci ");
		jpql.append("   join ci.state st ");
		jpql.append("  where rc.race = :race ");
		jpql.append("    and re.status = :status ");
		jpql.append("    and tf.user = pr.user ");
		jpql.append("  group by ");
		jpql.append("        ci.id, st.id ");
		jpql.append("  order by ");
		jpql.append("        count(re) desc, ci.name ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("race", event);
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
		jpql.append("   join re.raceCategory rc, ");
		jpql.append("        Profile pr ");
		jpql.append("  where rc.race = :race ");
		jpql.append("    and re.status = :status ");
		jpql.append("    and tf.user = pr.user ");
		jpql.append("  group by ");
		jpql.append("        pr.tshirt ");
		jpql.append("  order by ");
		jpql.append("        count(re) desc ");

		TypedQuery<EventAnalytic> query = getEntityManager().createQuery(jpql.toString(), EventAnalytic.class);
		query.setParameter("race", event);
		query.setParameter("status", CONFIRMED);

		return query.getResultList();
	}
}
