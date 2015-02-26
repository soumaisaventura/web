package adventure.persistence;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Race;
import adventure.entity.RaceOrganizer;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class RaceOrganizerDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static RaceOrganizerDAO getInstance() {
		return Beans.getReference(RaceOrganizerDAO.class);
	}

	public RaceOrganizer load(Race race, User organizer) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ro ");
		jpql.append("   from RaceOrganizer ro ");
		jpql.append("  where ro.race = :race ");
		jpql.append("    and ro.organizer = :organizer ");

		TypedQuery<RaceOrganizer> query = em.createQuery(jpql.toString(), RaceOrganizer.class);
		query.setParameter("race", race);
		query.setParameter("organizer", organizer);

		RaceOrganizer result;

		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}

	public void insert(RaceOrganizer raceOrganizer) {
		em.persist(raceOrganizer);
	}

	public void delete(RaceOrganizer raceOrganizer) {
		em.remove(raceOrganizer);
	}
}
