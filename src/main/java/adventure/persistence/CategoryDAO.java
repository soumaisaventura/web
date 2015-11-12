package adventure.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Category;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class CategoryDAO extends JPACrud<Category, Integer> {

	private static final long serialVersionUID = 1L;

	public static CategoryDAO getInstance() {
		return Beans.getReference(CategoryDAO.class);
	}

	public List<Category> findForEvent(Race race) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Category ( ");
		jpql.append("        c.id, ");
		jpql.append("        c.name, ");
		jpql.append("        c.description ");
		jpql.append("        ) ");
		jpql.append("   from RaceCategory rc ");
		jpql.append("   join rc.category c ");
		jpql.append("  where rc.race = :race ");
		jpql.append("    and rc.course.id = 0 ");
		jpql.append("  order by ");
		jpql.append("        c.teamSize, ");
		jpql.append("        c.name ");

		TypedQuery<Category> query = getEntityManager().createQuery(jpql.toString(), Category.class);
		query.setParameter("race", race);

		return query.getResultList();
	}

	// TODO: OLD
}
