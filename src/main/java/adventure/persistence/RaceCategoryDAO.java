package adventure.persistence;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Category;
import adventure.entity.Course;
import adventure.entity.Race;
import adventure.entity.RaceCategory;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class RaceCategoryDAO extends JPACrud<Category, Long> {

	private static final long serialVersionUID = 1L;

	public RaceCategory loadForRegister(Long raceId, Long courseId, Long categoryId) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new " + Find.class.getName()
				+ "(t.id, t.name, t.description, t.minMaleMembers, t.minFemaleMembers,, t.members, c.id, c.length) ");
		jpql.append("   from RaceCategory rc ");
		jpql.append("   join rc.race r ");
		jpql.append("   join rc.category t ");
		jpql.append("   left join ac.course c ");
		jpql.append("  where r.id = :raceId ");
		jpql.append("    and c.id = :courseId ");
		jpql.append("    and t.id = :categoryId ");
		jpql.append("  order by ");
		jpql.append("        c.length desc, ");
		jpql.append("        t.members desc, ");
		jpql.append("        t.name ");

		TypedQuery<RaceCategory> query = getEntityManager().createQuery(jpql.toString(), RaceCategory.class);
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

	public List<RaceCategory> find(Race race) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new " + Find.class.getName() + "(t.id, t.name, t.description, t.members, c.id, c.length) ");
		jpql.append("   from RaceCategory rc ");
		jpql.append("   join rc.race r ");
		jpql.append("   join rc.category t ");
		jpql.append("   left join ac.course c ");
		jpql.append("  where r = :race ");
		jpql.append("  order by ");
		jpql.append("        c.length desc, ");
		jpql.append("        t.members desc, ");
		jpql.append("        t.name ");

		TypedQuery<RaceCategory> query = getEntityManager().createQuery(jpql.toString(), RaceCategory.class);
		query.setParameter("race", race);

		return query.getResultList();
	}

	public static class Find extends RaceCategory {

		private static final long serialVersionUID = 1L;

		public Find(Long id, String name, String description, Integer minMalemembers, Integer minFemalemembers,
				Integer members, Long courseId, Integer courseLength) {
			setCategory(new Category());
			getCategory().setId(id);
			getCategory().setName(name);
			getCategory().setDescription(description);
			getCategory().setMinMaleMembers(minMalemembers);
			getCategory().setMinFemaleMembers(minFemalemembers);
			getCategory().setMembers(members);

			setCourse(new Course());
			getCourse().setId(courseId);
			getCourse().setLength(courseLength);
		}
	}
}
