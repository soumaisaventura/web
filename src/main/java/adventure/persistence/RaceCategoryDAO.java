package adventure.persistence;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.RaceCategory;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class RaceCategoryDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static RaceCategoryDAO getInstance() {
		return Beans.getReference(RaceCategoryDAO.class);
	}

	public RaceCategory loadForRegistration(Integer raceId, Integer courseId, Integer categoryId) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new RaceCategory( ");
		jpql.append("        t.id, ");
		jpql.append("        t.name, ");
		jpql.append("        t.description, ");
		jpql.append("        t.teamSize, ");
		jpql.append("        t.minMaleMembers, ");
		jpql.append("        t.minFemaleMembers, ");
		jpql.append("        c.id, ");
		jpql.append("        c.name, ");
		jpql.append("        c.annualFee, ");
		jpql.append("        r.id, ");
		jpql.append("        r.name, ");
		jpql.append("        r.date ");
		jpql.append("        ) ");
		jpql.append("   from RaceCategory rc ");
		jpql.append("   join rc.race r ");
		jpql.append("   join rc.category t ");
		jpql.append("   join rc.course c ");
		jpql.append("  where r.id = :raceId ");
		jpql.append("    and c.id = :courseId ");
		jpql.append("    and t.id = :categoryId ");
		jpql.append("  order by ");
		jpql.append("        c.name desc, ");
		jpql.append("        t.teamSize desc, ");
		jpql.append("        t.name ");

		TypedQuery<RaceCategory> query = em.createQuery(jpql.toString(), RaceCategory.class);
		query.setParameter("raceId", raceId);
		query.setParameter("courseId", courseId);
		query.setParameter("categoryId", categoryId);

		RaceCategory result;

		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}
}
