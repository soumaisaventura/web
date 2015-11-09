package adventure.persistence;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import adventure.entity.Championship;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class ChampionshipDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static ChampionshipDAO getInstance() {
		return Beans.getReference(ChampionshipDAO.class);
	}

	public List<Championship> findForEvent(Race race) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Championship ( ");
		jpql.append("        c.id, ");
		jpql.append("        c.name, ");
		jpql.append("        c.slug ");
		jpql.append("        ) ");
		jpql.append("   from ChampionshipRace cr ");
		jpql.append("   join cr.championship c ");
		jpql.append("  where cr.race = :race ");
		jpql.append("  order by ");
		jpql.append("        c.id ");

		TypedQuery<Championship> query = em.createQuery(jpql.toString(), Championship.class);
		query.setParameter("race", race);

		return query.getResultList();
	}

	// TODO: OLD
}
