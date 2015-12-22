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

	// Registration

	public RaceCategory load(Integer raceId, Integer categoryId) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new RaceCategory( ");
		jpql.append("        t.id, ");
		jpql.append("        t.name, ");
		jpql.append("        t.description, ");
		jpql.append("        t.teamSize, ");
		jpql.append("        t.minMaleMembers, ");
		jpql.append("        t.minFemaleMembers, ");
		jpql.append("        r.id, ");
		jpql.append("        r.name, ");
		jpql.append("        r.period.beginning, ");
		jpql.append("        r.period.end ");
		jpql.append("        ) ");
		jpql.append("   from RaceCategory rc ");
		jpql.append("   join rc.race r ");
		jpql.append("   join rc.category t ");
		jpql.append("  where r.id = :raceId ");
		jpql.append("    and t.id = :categoryId ");

		TypedQuery<RaceCategory> query = em.createQuery(jpql.toString(), RaceCategory.class);
		query.setParameter("raceId", raceId);
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
