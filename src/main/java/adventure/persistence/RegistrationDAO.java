package adventure.persistence;

import static adventure.entity.PaymentType.AUTO;
import static adventure.entity.RegistrationStatusType.CONFIRMED;
import static adventure.entity.RegistrationStatusType.PENDENT;
import static java.util.Calendar.YEAR;
import static javax.persistence.TemporalType.DATE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.AnnualFee;
import adventure.entity.AnnualFeePayment;
import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.TeamFormation;
import adventure.entity.User;
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

		return super.insert(registration);
	}

	@Override
	@Transactional
	public Registration update(Registration registration) {
		if (registration.getTeamName() != null) {
			registration.setTeamName(registration.getTeamName().trim());
		}

		if (registration.getStatus() == CONFIRMED) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(registration.getDate());
			Integer year = calendar.get(YEAR);
			AnnualFee annualFee = AnnualFeeDAO.getInstance().load(year);

			for (TeamFormation teamFormation : TeamFormationDAO.getInstance().find(registration)) {
				if (teamFormation.getAnnualFee().equals(annualFee.getFee())) {
					AnnualFeePayment annualFeePayment = new AnnualFeePayment();
					annualFeePayment.setRegistration(registration);
					annualFeePayment.setUser(UserDAO.getInstance().load(teamFormation.getUser().getId()));
					annualFeePayment.setAnnualFee(annualFee);

					AnnualFeePaymentDAO.getInstance().insert(annualFeePayment);
				}
			}
		}

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
		jpql.append("        re.paymentCode, ");
		jpql.append("        re.paymentTransaction, ");
		jpql.append("        su.id, ");
		jpql.append("        su.email, ");
		jpql.append("        pr.name, ");
		jpql.append("        re.status, ");
		jpql.append("        ra.id, ");
		jpql.append("        ra.name, ");
		jpql.append("        ra.date, ");
		jpql.append("        ra.paymentType, ");
		jpql.append("        ra.paymentInfo, ");
		jpql.append("        ra.paymentAccount, ");
		jpql.append("        ra.paymentToken, ");
		jpql.append("        pe.id, ");
		jpql.append("        pe.price, ");
		jpql.append("        ci.id, ");
		jpql.append("        ci.name, ");
		jpql.append("        st.id, ");
		jpql.append("        st.name, ");
		jpql.append("        st.abbreviation, ");
		jpql.append("        ca.id, ");
		jpql.append("        ca.name, ");
		jpql.append("        co.id, ");
		jpql.append("        co.name, ");
		jpql.append(" 	     (select min(_p.beginning) ");
		jpql.append(" 	        from Period _p ");
		jpql.append(" 	       where _p.race = ra), ");
		jpql.append(" 	     (select max(_p.end) ");
		jpql.append(" 	        from Period _p ");
		jpql.append(" 	       where _p.race = ra) ");
		jpql.append("        ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.submitter su ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join rc.course co ");
		jpql.append("   join rc.category ca ");
		jpql.append("   join re.period pe ");
		jpql.append("   left join ra.city ci ");
		jpql.append("   left join ci.state st, ");
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

	public List<Registration> findForUpdatePeriod() {
		StringBuffer jpql = new StringBuffer();

		// Long registrationId,
		// Date registrationDate,
		// String teamName,
		// String paymentCode,
		// String paymentTransaction,
		// Integer submitterId,
		// String submitterEmail,
		// String submitterName,
		// RegistrationStatusType registrationStatus,
		// Integer raceId,
		// String raceName,
		// Date raceDate,
		// PaymentType racePaymentType,
		// String racePaymentInfo,
		// String racePaymentAccount,
		// String racePaymentToken,
		// Integer periodId,
		// BigDecimal periodPrice,
		// Integer cityId,
		// String cityName,
		// Integer stateId,
		// String stateName,
		// String stateAbbreviation,
		// Integer categoryId,
		// String categoryName,
		// Integer courseId,
		// String courseName,
		// Date registrationPeriodBeginning,
		// Date registrationPeriodEnd

		jpql.append(" select ");
		jpql.append("    new Registration( ");
		jpql.append("        re.id, ");
		jpql.append("        re.date, ");
		jpql.append("        re.teamName, ");
		jpql.append("        re.paymentCode, ");
		jpql.append("        re.paymentTransaction, ");
		jpql.append("        su.id, ");
		jpql.append("        su.email, ");
		jpql.append("        pr.name, ");
		jpql.append("        re.status, ");
		jpql.append("        ra.id, ");
		jpql.append("        ra.name, ");
		jpql.append("        ra.date, ");
		jpql.append("        ra.paymentType, ");
		jpql.append("        ra.paymentInfo, ");
		jpql.append("        ra.paymentAccount, ");
		jpql.append("        ra.paymentToken, ");
		jpql.append("        pe.id, ");
		jpql.append("        pe.price, ");
		jpql.append("        ci.id, ");
		jpql.append("        ci.name, ");
		jpql.append("        st.id, ");
		jpql.append("        st.name, ");
		jpql.append("        st.abbreviation, ");
		jpql.append("        ca.id, ");
		jpql.append("        ca.name, ");
		jpql.append("        co.id, ");
		jpql.append("        co.name, ");
		jpql.append(" 	     (select min(_p.beginning) ");
		jpql.append(" 	        from Period _p ");
		jpql.append(" 	       where _p.race = ra), ");
		jpql.append(" 	     (select max(_p.end) ");
		jpql.append(" 	        from Period _p ");
		jpql.append(" 	       where _p.race = ra) ");
		jpql.append("        ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.submitter su ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join rc.course co ");
		jpql.append("   join rc.category ca ");
		jpql.append("   join re.period pe ");
		jpql.append("   left join ra.city ci ");
		jpql.append("   left join ci.state st, ");
		jpql.append("        Profile pr, ");
		jpql.append("        Period pe2 ");
		jpql.append("  where ra.paymentType = :paymentType ");
		jpql.append("    and su.id = pr.id ");
		jpql.append("    and not :date between pe.beginning and pe.end ");
		jpql.append("    and pe2.race = ra ");
		jpql.append("    and :date between pe2.beginning and pe2.end ");
		jpql.append("    and re.status = :status ");
		jpql.append("    and re.paymentTransaction is null ");

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql.toString(), Registration.class);
		query.setParameter("date", new Date(), DATE);
		query.setParameter("paymentType", AUTO);
		query.setParameter("status", PENDENT);

		return query.getResultList();
	}

	public List<Registration> findToOrganizer(Race race) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new TeamFormation( ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.mobile, ");
		jpql.append(" 	     p.tshirt, ");
		jpql.append(" 	     ci.id, ");
		jpql.append(" 	     ci.name, ");
		jpql.append(" 	     st.id, ");
		jpql.append(" 	     st.abbreviation, ");
		jpql.append(" 	     tf.racePrice, ");
		jpql.append(" 	     tf.annualFee, ");
		jpql.append(" 	     re.id, ");
		jpql.append(" 	     re.status, ");
		jpql.append(" 	     re.teamName, ");
		jpql.append(" 	     re.date, ");
		jpql.append(" 	     ra.id, ");
		jpql.append(" 	     ca.id, ");
		jpql.append(" 	     ca.name, ");
		jpql.append(" 	     co.id, ");
		jpql.append(" 	     co.name ");
		jpql.append(" 	     ) ");
		jpql.append("   from TeamFormation tf ");
		jpql.append("   join tf.user u ");
		jpql.append("   join tf.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join rc.category ca ");
		jpql.append("   join rc.course co, ");
		jpql.append("        Profile p ");
		jpql.append("   join p.city ci ");
		jpql.append("   join ci.state st ");
		jpql.append("  where u = p.user ");
		jpql.append("    and ra = :race ");
		jpql.append("  order by ");
		jpql.append("        re.id desc ");

		TypedQuery<TeamFormation> query = getEntityManager().createQuery(jpql.toString(), TeamFormation.class);
		query.setParameter("race", race);

		Registration registration = null;
		List<Registration> result = new ArrayList<Registration>();
		for (TeamFormation teamFormation : query.getResultList()) {
			if (!teamFormation.getRegistration().equals(registration)) {
				registration = teamFormation.getRegistration();
				registration.setTeamFormations(new ArrayList<TeamFormation>());
				result.add(registration);
			}

			teamFormation.setRegistration(null);
			registration.getTeamFormations().add(teamFormation);
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
		jpql.append("        ra.date, ");
		jpql.append("        ci.id, ");
		jpql.append("        ci.name, ");
		jpql.append("        st.abbreviation ");
		jpql.append("    ) ");
		jpql.append("   from TeamFormation tf ");
		jpql.append("   join tf.registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   left join ra.city ci ");
		jpql.append("   left join ci.state st ");
		jpql.append("  where tf.user = :user ");
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
		jpql.append("        ra.name, ");
		jpql.append("        ra.date, ");
		jpql.append("        ci.id, ");
		jpql.append("        ci.name, ");
		jpql.append("        st.abbreviation ");
		jpql.append("    ) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   left join ra.city ci ");
		jpql.append("   left join ci.state st ");
		jpql.append("  where ra = :race ");
		jpql.append("  order by ");
		jpql.append("        re.date ");

		TypedQuery<Registration> query = getEntityManager().createQuery(jpql.toString(), Registration.class);
		query.setParameter("race", race);

		return query.getResultList();
	}
}
