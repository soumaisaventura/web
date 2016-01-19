package adventure.persistence;

import static adventure.entity.EventPaymentType.AUTO;
import static adventure.entity.RegistrationStatusType.PENDENT;
import static javax.persistence.TemporalType.DATE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Event;
import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.entity.Registration;
import adventure.entity.User;
import adventure.entity.UserRegistration;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class RegistrationDAO extends JPACrud<Registration, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Registration insert(Registration registration) {
		if (registration.getTeamName() != null) {
			registration.setTeamName(registration.getTeamName().trim());
		}

		RaceCategory raceCategory = registration.getRaceCategory();
		raceCategory.setCategory(CategoryDAO.getInstance().load(raceCategory.getCategory().getId()));
		raceCategory.setRace(RaceDAO.getInstance().load(raceCategory.getRace().getId()));

		return super.insert(registration);
	}

	@Override
	@Transactional
	public Registration update(Registration registration) {
		if (registration.getTeamName() != null) {
			registration.setTeamName(registration.getTeamName().trim());
		}

		RaceCategory raceCategory = registration.getRaceCategory();
		raceCategory.setCategory(CategoryDAO.getInstance().load(raceCategory.getCategory().getId()));
		raceCategory.setRace(RaceDAO.getInstance().load(raceCategory.getRace().getId()));

		return super.update(registration);
	}

	public static RegistrationDAO getInstance() {
		return Beans.getReference(RegistrationDAO.class);
	}

	public Registration loadForDetails(Long id) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Registration( ");
		jpql.append("        re.id, ");
		jpql.append("        re.date, ");
		jpql.append("        re.teamName, ");
		jpql.append("        re.status, ");
		jpql.append("        re.payment.code, ");
		jpql.append("        re.payment.transaction, ");
		jpql.append("        pe.id, ");
		jpql.append("        pe.price, ");
		jpql.append("        su.id, ");
		jpql.append("        su.email, ");
		jpql.append("        pr.name, ");
		jpql.append("        pr.mobile, ");
		jpql.append("        ra.id, ");
		jpql.append("        ra.name, ");
		jpql.append("        ra.description, ");
		jpql.append("        ra.slug, ");
		jpql.append("        ra.distance, ");
		jpql.append("        sa.id, ");
		jpql.append("        sa.name, ");
		jpql.append("        ra.period.beginning, ");
		jpql.append("        ra.period.end, ");
		jpql.append("        ev.id, ");
		jpql.append("        ev.name, ");
		jpql.append("        ev.slug, ");
		jpql.append("        ev.payment.type, ");
		jpql.append("        ev.payment.info, ");
		jpql.append("        ev.payment.account, ");
		jpql.append("        ev.payment.token, ");
		// jpql.append("        ev.coords.latitude, ");
		// jpql.append("        ev.coords.longitude, ");
		jpql.append("        ci.id, ");
		jpql.append("        ci.name, ");
		jpql.append("        st.id, ");
		jpql.append("        st.name, ");
		jpql.append("        st.abbreviation, ");
		jpql.append("        ca.id, ");
		jpql.append("        ca.name ");
		jpql.append("        ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.submitter su ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join re.period pe ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.status sa ");
		jpql.append("   join rc.category ca ");
		jpql.append("   join ra.event ev ");
		jpql.append("   join ev.city ci ");
		jpql.append("   join ci.state st, ");
		jpql.append("        Profile pr ");
		jpql.append("  where su.id = pr.id ");
		jpql.append("    and re.id = :id");

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql.toString(), Registration.class);
		query.setParameter("id", id);

		Registration result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public Registration loadForMeta(Long id) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Registration( ");
		jpql.append(" 	        re.id, ");
		jpql.append(" 	        ev.id, ");
		jpql.append(" 	        ev.name, ");
		jpql.append(" 	        ev.slug, ");
		jpql.append(" 	        ev.beginning, ");
		jpql.append(" 	        ev.end ");
		jpql.append("        ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.event ev ");
		jpql.append("  where re.id = :id");

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql.toString(), Registration.class);
		query.setParameter("id", id);

		Registration result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public List<Registration> findForUpdatePeriod() {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Registration( ");
		jpql.append("        re.id, ");
		jpql.append("        re.date, ");
		jpql.append("        re.teamName, ");
		jpql.append("        re.status, ");
		jpql.append("        re.payment.code, ");
		jpql.append("        re.payment.transaction, ");
		jpql.append("        pe.id, ");
		jpql.append("        pe.price, ");
		jpql.append("        su.id, ");
		jpql.append("        su.email, ");
		jpql.append("        pr.name, ");
		jpql.append("        pr.mobile, ");
		jpql.append("        ra.id, ");
		jpql.append("        ra.name, ");
		jpql.append("        ra.description, ");
		jpql.append("        ra.slug, ");
		jpql.append("        ra.distance, ");
		jpql.append("        sa.id, ");
		jpql.append("        sa.name, ");
		jpql.append("        ra.period.beginning, ");
		jpql.append("        ra.period.end, ");
		jpql.append("        ev.id, ");
		jpql.append("        ev.name, ");
		jpql.append("        ev.slug, ");
		jpql.append("        ev.payment.type, ");
		jpql.append("        ev.payment.info, ");
		jpql.append("        ev.payment.account, ");
		jpql.append("        ev.payment.token, ");
		// jpql.append("        ev.coords.latitude, ");
		// jpql.append("        ev.coords.longitude, ");
		jpql.append("        ci.id, ");
		jpql.append("        ci.name, ");
		jpql.append("        st.id, ");
		jpql.append("        st.name, ");
		jpql.append("        st.abbreviation, ");
		jpql.append("        ca.id, ");
		jpql.append("        ca.name ");
		jpql.append("        ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.submitter su ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join re.period pe ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.period pe2 ");
		jpql.append("   join ra.status sa ");
		jpql.append("   join rc.category ca ");
		jpql.append("   join ra.event ev ");
		jpql.append("   join ev.city ci ");
		jpql.append("   join ci.state st, ");
		jpql.append("        Profile pr ");
		jpql.append("  where su.id = pr.id ");
		jpql.append("    and ev.payment.type = :paymentType ");
		jpql.append("    and not :date between pe.beginning and pe.end ");
		jpql.append("    and :date between pe2.beginning and pe2.end ");
		jpql.append("    and re.status = :status ");
		jpql.append("    and re.payment.transaction is null ");

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql.toString(), Registration.class);
		query.setParameter("date", new Date(), DATE);
		query.setParameter("paymentType", AUTO);
		query.setParameter("status", PENDENT);

		return query.getResultList();
	}

	public List<Registration> findToOrganizer(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new UserRegistration( ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.mobile, ");
		jpql.append(" 	     p.tshirt, ");
		jpql.append(" 	     p.birthday, ");
		jpql.append(" 	     p.rg, ");
		jpql.append(" 	     p.cpf, ");
		jpql.append(" 	     ci.id, ");
		jpql.append(" 	     ci.name, ");
		jpql.append(" 	     st.id, ");
		jpql.append(" 	     st.abbreviation, ");
		jpql.append(" 	     tf.racePrice, ");
		jpql.append(" 	     re.id, ");
		jpql.append(" 	     re.status, ");
		jpql.append(" 	     re.teamName, ");
		jpql.append(" 	     re.date, ");
		jpql.append(" 	     ra.id, ");
		jpql.append(" 	     ra.slug, ");
		jpql.append(" 	     ra.name, ");
		jpql.append(" 	     ca.id, ");
		jpql.append(" 	     ca.name ");
		jpql.append(" 	     ) ");
		jpql.append("   from UserRegistration tf ");
		jpql.append("   join tf.user u ");
		jpql.append("   join tf.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.event ev ");
		jpql.append("   join rc.category ca, ");
		jpql.append("        Profile p ");
		jpql.append("   join p.city ci ");
		jpql.append("   join ci.state st ");
		jpql.append("  where u = p.user ");
		jpql.append("    and ev = :event ");
		jpql.append("  order by ");
		jpql.append("        re.id desc ");

		TypedQuery<UserRegistration> query = getEntityManager().createQuery(jpql.toString(), UserRegistration.class);
		query.setParameter("event", event);

		Registration registration = null;
		List<Registration> result = new ArrayList<Registration>();
		for (UserRegistration teamFormation : query.getResultList()) {
			if (!teamFormation.getRegistration().equals(registration)) {
				registration = teamFormation.getRegistration();
				registration.setUserRegistrations(new ArrayList<UserRegistration>());
				result.add(registration);
			}

			teamFormation.setRegistration(null);
			registration.getUserRegistrations().add(teamFormation);
		}

		return result;
	}

	public List<Registration> find(User loggedInUser) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select  ");
		jpql.append("    new Registration( ");
		jpql.append("        re.id, ");
		jpql.append("        re.status, ");
		jpql.append("        re.teamName, ");
		jpql.append("        ra.id, ");
		jpql.append("        ra.name, ");
		jpql.append("        ra.description, ");
		jpql.append("        ra.slug, ");
		jpql.append("        ra.period.beginning, ");
		jpql.append("        ra.period.end, ");
		jpql.append("        ev.id, ");
		jpql.append("        ev.name, ");
		jpql.append("        ev.slug, ");
		jpql.append("        ci.id, ");
		jpql.append("        ci.name, ");
		jpql.append("        st.abbreviation ");
		jpql.append("    ) ");
		jpql.append("   from UserRegistration ur ");
		jpql.append("   join ur.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join ra.event ev ");
		jpql.append("   join ev.city ci ");
		jpql.append("   join ci.state st ");
		jpql.append("  where ur.user = :user ");
		jpql.append("  order by ");
		jpql.append("        re.date desc ");

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql.toString(), Registration.class);
		query.setParameter("user", loggedInUser);

		return query.getResultList();
	}

	public List<Registration> findToUser(Race race) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select  ");
		jpql.append("    new Registration( ");
		jpql.append("        re.id, ");
		jpql.append("        re.status, ");
		jpql.append("        re.teamName, ");
		jpql.append("        ra.id, ");
		jpql.append("        ra.name ");
		// jpql.append("        ra.date, ");
		// jpql.append("        ci.id, ");
		// jpql.append("        ci.name, ");
		// jpql.append("        st.abbreviation ");
		jpql.append("    ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		// jpql.append("   left join ra.city ci ");
		// jpql.append("   left join ci.state st ");
		jpql.append("  where ra = :race ");
		jpql.append("  order by ");
		jpql.append("        re.date ");

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql.toString(), Registration.class);
		query.setParameter("race", race);

		return query.getResultList();
	}
}
