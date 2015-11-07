package adventure.persistence;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import adventure.entity.Modality;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class ModalityDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static ModalityDAO getInstance() {
		return Beans.getReference(ModalityDAO.class);
	}

	public List<Modality> findForEvent(Race race) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Modality ( ");
		jpql.append("        m.id, ");
		jpql.append("        m.name, ");
		jpql.append("        m.acronym ");
		jpql.append("        ) ");
		jpql.append("   from RaceModality rm ");
		jpql.append("   join rm.modality m ");
		jpql.append("  where rm.race = :race ");
		jpql.append("  order by ");
		jpql.append("        m.name ");

		TypedQuery<Modality> query = em.createQuery(jpql.toString(), Modality.class);
		query.setParameter("race", race);

		return query.getResultList();
	}

	// TODO: OLD
}
