package adventure.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import adventure.security.User;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Strings;

@Transactional
public class UserDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public List<User> search(String filter, List<Long> excludeIds) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	new " + User.class.getName() + "(a.id, a.email, p.name, p.gender) ");
		jpql.append(" from ");
		jpql.append(" 	Profile p join p.account a");
		jpql.append(" where a.id not in :exclusion ");
		jpql.append("   and lower(p.name) like :filter ");

		TypedQuery<User> query = em.createQuery(jpql.toString(), User.class);
		query.setMaxResults(10);

		ArrayList<Long> exclusion = new ArrayList<Long>();
		exclusion.add(Long.valueOf(0));
		exclusion.addAll(excludeIds);

		query.setParameter("exclusion", exclusion);
		query.setParameter("filter", Strings.isEmpty(filter) ? "" : "%" + filter.toLowerCase() + "%");

		return query.getResultList();
	}
}
