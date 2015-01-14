package adventure.persistence;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Register;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class RegisterDAO extends JPACrud<Register, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Register insert(Register register) {
		register.setDate(new Date());
		return super.insert(register);
	}

	public Register loadForEmail(Long id) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Register( ");
		jpql.append("       re.id as registerId, ");
		jpql.append("       re.date as registerDate, ");
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
		jpql.append("   from Register re ");
		jpql.append("   join re.creator cr ");
		jpql.append("   join re.raceCategory rc ");
		jpql.append("   join rc.race ra ");
		jpql.append("   join rc.course co ");
		jpql.append("   join rc.category ca, ");
		jpql.append("        Profile pr ");
		jpql.append("  where cr.id = pr.id ");
		jpql.append("    and re.id = :id");

		TypedQuery<Register> query = getEntityManager().createQuery(jpql.toString(), Register.class);
		query.setParameter("id", id);

		Register result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}
}
