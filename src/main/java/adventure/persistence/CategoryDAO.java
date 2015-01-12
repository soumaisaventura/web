package adventure.persistence;

import adventure.entity.Category;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class CategoryDAO extends JPACrud<Category, Long> {

	private static final long serialVersionUID = 1L;

	// public List<Category> find(Race race) throws Exception {
	// StringBuffer jpql = new StringBuffer();
	// jpql.append(" select ");
	// jpql.append("    new " + FindByRace.class.getName()
	// + "(t.id, t.name, t.description, t.members, c.id, c.length) ");
	// jpql.append("   from AvailableCategory ac ");
	// jpql.append("   join ac.race r ");
	// jpql.append("   join ac.category t ");
	// jpql.append("   left join ac.course c ");
	// jpql.append("  where r = :race ");
	// jpql.append("  order by ");
	// jpql.append("        c.length desc, ");
	// jpql.append("        t.members desc, ");
	// jpql.append("        t.name ");
	//
	// TypedQuery<Category> query = getEntityManager().createQuery(jpql.toString(), Category.class);
	// query.setParameter("race", race);
	//
	// return query.getResultList();
	// }
	//
	// public static class FindByRace extends Category {
	//
	// private static final long serialVersionUID = 1L;
	//
	// public FindByRace(Long id, String name, String description, Integer members, Long courseId, Integer courseLength)
	// {
	// setId(id);
	// setName(name);
	// setDescription(description);
	// setMembers(members);
	//
	// Course course = new Course();
	// course.setId(courseId);
	// course.setLength(courseLength);
	// setCourse(course);
	// }
	// }
}
