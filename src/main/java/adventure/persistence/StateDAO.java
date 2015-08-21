package adventure.persistence;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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
	
	public State load(String abbreviation){
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new State( ");
		jpql.append("        s.id, ");
		jpql.append("        s.name, ");
		jpql.append("        s.abbreviation ");
		jpql.append("        ) ");
		jpql.append("   from State s ");
		jpql.append("  where s.abbreviation = :abbreviation ");
		jpql.append("  order by s.abbreviation ");

		TypedQuery<State> query = getEntityManager().createQuery(jpql.toString(), State.class);
		query.setParameter("abbreviation", abbreviation);

		State result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
		
	}

}
