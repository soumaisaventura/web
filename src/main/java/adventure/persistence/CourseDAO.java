package adventure.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Category;
import adventure.entity.Course;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class CourseDAO extends JPACrud<Course, Long> {

	private static final long serialVersionUID = 1L;

	public List<Course> findWithCategories(Race race) throws Exception {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new " + LoadWithCategories.class.getName() + "( ");
		jpql.append("        t.id, ");
		jpql.append("        t.name, ");
		jpql.append("        t.description, ");
		jpql.append("        t.teamSize, ");
		jpql.append("        t.minMaleMembers, ");
		jpql.append("        t.minFemaleMembers, ");
		jpql.append("        c.id, ");
		jpql.append("        c.length, ");
		jpql.append("        r.id) ");
		jpql.append("   from RaceCategory rc ");
		jpql.append("   join rc.race r ");
		jpql.append("   join rc.category t ");
		jpql.append("   join rc.course c ");
		jpql.append("  where r = :race ");
		jpql.append("  order by ");
		jpql.append("        c.length desc, ");
		jpql.append("        c.id, ");
		jpql.append("        t.teamSize desc, ");
		jpql.append("        t.name ");

		TypedQuery<LoadWithCategories> query = getEntityManager()
				.createQuery(jpql.toString(), LoadWithCategories.class);
		query.setParameter("race", race);

		List<Course> result = new ArrayList<Course>();
		Long previousCourseId = null;
		Course course = null;

		for (LoadWithCategories row : query.getResultList()) {
			if (!row.course.getId().equals(previousCourseId)) {
				course = new Course();
				course.setId(row.course.getId());
				course.setLength(row.course.getLength());
				course.setCategories(new ArrayList<Category>());
			}

			Category category = new Category();
			category.setId(row.category.getId());
			category.setName(row.category.getName());
			category.setDescription(row.category.getDescription());
			category.setTeamSize(row.category.getTeamSize());
			category.setMinMaleMembers(row.category.getMinMaleMembers());
			category.setMinFemaleMembers(row.category.getMinFemaleMembers());
			course.getCategories().add(category);

			if (!row.course.getId().equals(previousCourseId)) {
				previousCourseId = course.getId();
				result.add(course);
			}
		}

		return result;
	}

	public static class LoadWithCategories implements Serializable {

		private static final long serialVersionUID = 1L;

		Course course = new Course();

		Category category = new Category();

		Race race = new Race();

		public LoadWithCategories(Long categoryId, String categoryName, String categoryDescription,
				Integer categoryTeamSize, Integer categoryMinMaleMembers, Integer categoryMinFemaleMembers,
				Long courseId, Integer courseLength, Long raceId) throws Exception {
			course.setId(courseId);
			course.setLength(courseLength);

			category.setId(categoryId);
			category.setName(categoryName);
			category.setDescription(categoryDescription);
			category.setTeamSize(categoryTeamSize);
			category.setMinMaleMembers(categoryMinMaleMembers);
			category.setMinFemaleMembers(categoryMinFemaleMembers);

			race.setId(raceId);
		}
	}
}
