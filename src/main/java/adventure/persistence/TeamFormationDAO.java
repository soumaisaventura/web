package adventure.persistence;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import adventure.entity.Account;
import adventure.entity.Race;
import adventure.entity.TeamFormation;
import adventure.security.User;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class TeamFormationDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public TeamFormation insert(TeamFormation entity) {
		em.persist(entity);
		return entity;
	}

	public List<TeamFormation> find(Race race, Account member) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	new " + User.class.getName() + "(a.id, a.email, p.name, p.gender) ");
		jpql.append(" from ");
		jpql.append(" 	Profile p join p.account a");
		jpql.append(" where a.confirmation is not null ");
		jpql.append("   and a.id not in :exclusion ");
		jpql.append("   and lower(p.name) like :filter ");

		TypedQuery<TeamFormation> query = em.createQuery(jpql.toString(), TeamFormation.class);
//		query.setMaxResults(10);

		return query.getResultList();
	}
}
