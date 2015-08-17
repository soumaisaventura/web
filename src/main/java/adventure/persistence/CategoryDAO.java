package adventure.persistence;

import adventure.entity.Category;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class CategoryDAO extends JPACrud<Category, Integer> {

	private static final long serialVersionUID = 1L;

	public static CategoryDAO getInstance() {
		return Beans.getReference(CategoryDAO.class);
	}
}
