package adventure.persistence;

import adventure.entity.*;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static adventure.entity.EventPaymentType.AUTO;
import static adventure.entity.RegistrationStatusType.PENDENT;
import static adventure.entity.Status.OPEN_ID;
import static javax.persistence.TemporalType.DATE;

@Transactional
public class RegistrationDAO extends JPACrud<Registration, Long> {

	private static final long serialVersionUID = 1L;

	public static RegistrationDAO getInstance() {
		return Beans.getReference(RegistrationDAO.class);
	}

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

	public Registration loadForDetails(Long id) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new Registration( ";
		jpql += "        re.id, ";
		jpql += "        re.date, ";
		jpql += "        re.teamName, ";
		jpql += "        re.status, ";
		jpql += "        re.payment.checkoutCode, ";
		jpql += "        re.payment.transactionCode, ";
		jpql += "        pe.id, ";
		jpql += "        pe.price, ";
		jpql += "        su.id, ";
		jpql += "        su.email, ";
		jpql += "        pr.name, ";
		jpql += "        pr.mobile, ";
		jpql += "        ra.id, ";
		jpql += "        ra.name, ";
		jpql += "        ra.description, ";
		jpql += "        ra.alias, ";
		jpql += "        ra.distance, ";
		jpql += "        sa.id, ";
		jpql += "        sa.name, ";
		jpql += "        ra.period.beginning, ";
		jpql += "        ra.period.end, ";
		jpql += "        ev.id, ";
		jpql += "        ev.name, ";
		jpql += "        ev.alias, ";
		jpql += "        ev.payment.type, ";
		jpql += "        ev.payment.info, ";
		jpql += "        ev.payment.account, ";
		jpql += "        ev.payment.token, ";
		jpql += "        ci.id, ";
		jpql += "        ci.name, ";
		jpql += "        st.id, ";
		jpql += "        st.name, ";
		jpql += "        st.abbreviation, ";
		jpql += "        ca.id, ";
		jpql += "        ca.alias, ";
        jpql += "        ca.name, ";
        jpql += "        ca.teamSize ";
        jpql += "        ) ";
		jpql += "   from Registration re ";
		jpql += "   join re.submitter su ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join re.period pe ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.status sa ";
		jpql += "   join rc.category ca ";
		jpql += "   join ra.event ev ";
		jpql += "   join ev.city ci ";
		jpql += "   join ci.state st, ";
		jpql += "        Profile pr ";
		jpql += "  where su.id = pr.id ";
		jpql += "    and re.id = :id";

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql, Registration.class);
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
		String jpql = "";
		jpql += " select ";
		jpql += "    new Registration( ";
		jpql += " 	        re.id, ";
		jpql += " 	        ev.id, ";
		jpql += " 	        ev.name, ";
		jpql += " 	        ev.alias, ";
		jpql += " 	        ev.beginning, ";
		jpql += " 	        ev.end ";
		jpql += "        ) ";
		jpql += "   from Registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.event ev ";
		jpql += "  where re.id = :id";

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql, Registration.class);
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
		String jpql = "";
		jpql += " select ";
		jpql += "    new Registration( ";
		jpql += "        re.id, ";
		jpql += "        re.date, ";
		jpql += "        re.teamName, ";
		jpql += "        re.status, ";
		jpql += "        re.payment.checkoutCode, ";
		jpql += "        re.payment.transactionCode, ";
		jpql += "        pe.id, ";
		jpql += "        pe.price, ";
		jpql += "        su.id, ";
		jpql += "        su.email, ";
		jpql += "        pr.name, ";
		jpql += "        pr.mobile, ";
		jpql += "        ra.id, ";
		jpql += "        ra.name, ";
		jpql += "        ra.description, ";
		jpql += "        ra.alias, ";
		jpql += "        ra.distance, ";
		jpql += "        sa.id, ";
		jpql += "        sa.name, ";
		jpql += "        ra.period.beginning, ";
		jpql += "        ra.period.end, ";
		jpql += "        ev.id, ";
		jpql += "        ev.name, ";
		jpql += "        ev.alias, ";
		jpql += "        ev.payment.type, ";
		jpql += "        ev.payment.info, ";
		jpql += "        ev.payment.account, ";
		jpql += "        ev.payment.token, ";
		jpql += "        ci.id, ";
		jpql += "        ci.name, ";
		jpql += "        st.id, ";
		jpql += "        st.name, ";
		jpql += "        st.abbreviation, ";
		jpql += "        ca.id, ";
		jpql += "        ca.alias, ";
        jpql += "        ca.name, ";
        jpql += "        ca.teamSize ";
        jpql += "        ) ";
		jpql += "   from Registration re ";
		jpql += "   join re.submitter su ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join re.period pe ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.status sa ";
		jpql += "   join rc.category ca ";
		jpql += "   join ra.event ev ";
		jpql += "   join ev.city ci ";
		jpql += "   join ci.state st, ";
		jpql += "        Profile pr ";
		jpql += "  where su.id = pr.id ";
		jpql += "    and ev.payment.type = :paymentType ";
		jpql += "    and not :date between pe.beginning and pe.end ";
		jpql += "    and ra.status.id = :raceOpen ";
		jpql += "    and re.status = :status ";
		jpql += "    and re.payment.transactionCode is null ";

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql, Registration.class);
		query.setParameter("date", new Date(), DATE);
		query.setParameter("paymentType", AUTO);
		query.setParameter("status", PENDENT);
		query.setParameter("raceOpen", OPEN_ID);

		return query.getResultList();
	}

	public List<Registration> findToOrganizer(Event event) {
		String jpql = "";
		jpql += " select ";
		jpql += " 	 new UserRegistration( ";
		jpql += " 	     u.id, ";
		jpql += " 	     u.email, ";
		jpql += " 	     p.name, ";
		jpql += " 	     p.mobile, ";
		jpql += " 	     p.tshirt, ";
		jpql += " 	     k.id, ";
		jpql += " 	     k.name, ";
		jpql += " 	     p.birthday, ";
		jpql += " 	     p.rg, ";
		jpql += " 	     p.cpf, ";
		jpql += " 	     ci.id, ";
		jpql += " 	     ci.name, ";
		jpql += " 	     st.id, ";
		jpql += " 	     st.abbreviation, ";
		jpql += " 	     tf.amount, ";
		jpql += " 	     re.id, ";
		jpql += " 	     re.status, ";
		jpql += " 	     re.teamName, ";
		jpql += " 	     re.date, ";
		jpql += " 	     ra.id, ";
		jpql += " 	     ra.alias, ";
		jpql += " 	     ra.name, ";
		jpql += " 	     ca.id, ";
		jpql += " 	     ca.alias, ";
		jpql += " 	     ca.name ";
		jpql += " 	     ) ";
		jpql += "   from UserRegistration tf ";
		jpql += "   left join tf.kit k ";
		jpql += "   join tf.user u ";
		jpql += "   join tf.registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.event ev ";
		jpql += "   join rc.category ca, ";
		jpql += "        Profile p ";
		jpql += "   join p.city ci ";
		jpql += "   join ci.state st ";
		jpql += "  where u = p.user ";
		jpql += "    and ev = :event ";
		jpql += "  order by ";
		jpql += "        re.id desc ";

		TypedQuery<UserRegistration> query = getEntityManager().createQuery(jpql, UserRegistration.class);
		query.setParameter("event", event);

		Registration registration = null;
		List<Registration> result = new ArrayList();
		for (UserRegistration teamFormation : query.getResultList()) {
			if (!teamFormation.getRegistration().equals(registration)) {
				registration = teamFormation.getRegistration();
				registration.setUserRegistrations(new ArrayList());
				result.add(registration);
			}

			teamFormation.setRegistration(null);
			registration.getUserRegistrations().add(teamFormation);
		}

		return result;
	}

	public List<Registration> find(User loggedInUser) {
		String jpql = "";
		jpql += " select  ";
		jpql += "    new Registration( ";
		jpql += "        re.id, ";
		jpql += "        re.status, ";
		jpql += "        re.teamName, ";
		jpql += "        ra.id, ";
		jpql += "        ra.name, ";
		jpql += "        ra.description, ";
		jpql += "        ra.alias, ";
		jpql += "        ra.period.beginning, ";
		jpql += "        ra.period.end, ";
		jpql += "        ev.id, ";
		jpql += "        ev.name, ";
		jpql += "        ev.alias, ";
		jpql += "        ci.id, ";
		jpql += "        ci.name, ";
		jpql += "        st.abbreviation ";
		jpql += "    ) ";
		jpql += "   from UserRegistration ur ";
		jpql += "   join ur.registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		jpql += "   join ra.event ev ";
		jpql += "   join ev.city ci ";
		jpql += "   join ci.state st ";
		jpql += "  where ur.user = :user ";
		jpql += "  order by ";
		jpql += "        re.date desc ";

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql, Registration.class);
		query.setParameter("user", loggedInUser);

		return query.getResultList();
	}

	public List<Registration> findToUser(Race race) {
		String jpql = "";
		jpql += " select  ";
		jpql += "    new Registration( ";
		jpql += "        re.id, ";
		jpql += "        re.status, ";
		jpql += "        re.teamName, ";
		jpql += "        ra.id, ";
		jpql += "        ra.name ";
		// jpql += "        ra.date, ";
		// jpql += "        ci.id, ";
		// jpql += "        ci.name, ";
		// jpql += "        st.abbreviation ";
		jpql += "    ) ";
		jpql += "   from Registration re ";
		jpql += "   join re.raceCategory rc ";
		jpql += "   join rc.race ra ";
		// jpql += "   left join ra.city ci ";
		// jpql += "   left join ci.state st ";
		jpql += "  where ra = :race ";
		jpql += "  order by ";
		jpql += "        re.date ";

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql, Registration.class);
		query.setParameter("race", race);

		return query.getResultList();
	}
}
