package adventure.persistence;

import adventure.entity.State;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class StateDAO extends JPACrud<State, Integer> {

	private static final long serialVersionUID = 1L;

	public static StateDAO getInstance() {
		return Beans.getReference(StateDAO.class);
	}

}
