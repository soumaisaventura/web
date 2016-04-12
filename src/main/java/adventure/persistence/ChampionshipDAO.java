package adventure.persistence;

import adventure.entity.Championship;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Transactional
public class ChampionshipDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static ChampionshipDAO getInstance() {
		return Beans.getReference(ChampionshipDAO.class);
	}

	public List<Championship> findForEvent(Race race) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new Championship ( ";
		jpql += "        c.id, ";
		jpql += "        c.name, ";
		jpql += "        c.alias ";
		jpql += "        ) ";
		jpql += "   from ChampionshipRace cr ";
		jpql += "   join cr.championship c ";
		jpql += "  where cr.race = :race ";
		jpql += "  order by ";
		jpql += "        c.id ";

		TypedQuery<Championship> query = em.createQuery(jpql, Championship.class);
		query.setParameter("race", race);

		return query.getResultList();
	}

	// TODO: OLD
}
