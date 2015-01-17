package adventure.persistence;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Registration;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class RegistrationDAO extends JPACrud<Registration, Long> {

	private static final long serialVersionUID = 1L;

	public static RegistrationDAO getInstance() {
		return Beans.getReference(RegistrationDAO.class);
	}

	@Override
	public Registration insert(Registration registration) {
		registration.setDate(new Date());
		return super.insert(registration);
	}

	public Registration loadForMail(Long id) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Registration( ");
		jpql.append("       re.id as registrationId, ");
		jpql.append("       re.date as registrationDate, ");
		jpql.append("       re.teamName, ");
		jpql.append("       cr.id as creatorId, ");
		jpql.append("       pr.name as creatorName, ");
		jpql.append("       ra.id as raceId, ");
		jpql.append("       ra.name as raceName, ");
		jpql.append("       ra.date as raceDate, ");
		jpql.append("       ca.id as categoryId, ");
		jpql.append("       ca.name as categoryName, ");
		jpql.append("       co.id as courseId, ");
		jpql.append("       co.length as courseLength) ");
		jpql.append("   from Registration re ");
		jpql.append("   join re.creator cr ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join rc.course co ");
		jpql.append("   join rc.category ca, ");
		jpql.append("        Profile pr ");
		jpql.append("  where cr.id = pr.id ");
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
}
