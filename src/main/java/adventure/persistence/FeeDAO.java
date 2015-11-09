package adventure.persistence;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import adventure.entity.Championship;
import adventure.entity.Fee;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class FeeDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static FeeDAO getInstance() {
		return Beans.getReference(FeeDAO.class);
	}

	public List<Fee> findForEvent(Race race) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Fee ( ");
		jpql.append("        f.id, ");
		jpql.append("        f.name, ");
		jpql.append("        f.value, ");
		jpql.append("        f.percentage, ");
		jpql.append("        f.optional ");
		jpql.append("        ) ");
		jpql.append("   from RaceFee rf ");
		jpql.append("   join rf.fee f ");
		jpql.append("  where rf.race = :race ");
		jpql.append("  order by ");
		jpql.append("        f.id ");

		TypedQuery<Fee> query = em.createQuery(jpql.toString(), Fee.class);
		query.setParameter("race", race);

		return query.getResultList();
	}

	public List<Fee> findForEvent(Championship championship) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new Fee ( ");
		jpql.append("        f.id, ");
		jpql.append("        f.name, ");
		jpql.append("        f.value, ");
		jpql.append("        f.percentage, ");
		jpql.append("        f.optional, ");
		jpql.append("        cf.once ");
		jpql.append("        ) ");
		jpql.append("   from ChampionshipFee cf ");
		jpql.append("   join cf.fee f ");
		jpql.append("  where cf.championship = :championship ");
		jpql.append("  order by ");
		jpql.append("        f.id ");

		TypedQuery<Fee> query = em.createQuery(jpql.toString(), Fee.class);
		query.setParameter("championship", championship);

		return query.getResultList();
	}

	// TODO: OLD
}
