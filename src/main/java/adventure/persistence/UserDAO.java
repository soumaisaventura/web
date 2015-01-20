package adventure.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Race;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

@Transactional
public class UserDAO extends JPACrud<User, Long> {

	private static final long serialVersionUID = 1L;

	public static UserDAO getInstance() {
		return Beans.getReference(UserDAO.class);
	}

	@Override
	public User insert(User user) {
		user.setCreation(new Date());

		if (user.getEmail() != null) {
			user.setEmail(user.getEmail().trim());
		}

		return super.insert(user);
	}

	@Override
	public void delete(Long id) {
		User user = load(id);

		if (user != null) {
			user.setDeleted(new Date());
			update(user);
		}
	}

	public List<User> search(String filter, List<Long> excludeIds) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new User( ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.gender, ");
		jpql.append(" 	     p.pendencies, ");
		jpql.append(" 	     h.pendencies ");
		jpql.append(" 	     ) ");
		jpql.append("   from Profile p ");
		jpql.append("   join p.user u, ");
		jpql.append("        Health h");
		jpql.append("  where u = h.user ");
		jpql.append("    and u.activation is not null ");
		jpql.append("    and lower(p.name) like :filter ");
		jpql.append("    and u.id not in :excludeIds ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setMaxResults(10);

		if (excludeIds.isEmpty()) {
			excludeIds.add(Long.valueOf(-1));
		}

		query.setParameter("excludeIds", excludeIds);
		query.setParameter("filter", Strings.isEmpty(filter) ? "" : "%" + filter.toLowerCase() + "%");

		return query.getResultList();
	}

	public List<User> findRaceOrganizers(Race race) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new User(o.id, o.email, p.name, p.gender) ");
		jpql.append("   from RaceOrganizer ro ");
		jpql.append("   join ro.organizer o, ");
		jpql.append("        Profile p ");
		jpql.append("  where o = p.user ");
		jpql.append("    and ro.race = :race ");
		jpql.append("  order by ");
		jpql.append("        p.name ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("race", race);

		return query.getResultList();
	}

	public User loadForAuthentication(String email) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new User( ");
		jpql.append("        u.id, ");
		jpql.append("        u.email, ");
		jpql.append("        u.password, ");
		jpql.append("        u.activation, ");
		jpql.append("        u.activationToken, ");
		jpql.append("        p.name, ");
		jpql.append("        p.gender, ");
		jpql.append(" 	     p.pendencies, ");
		jpql.append(" 	     h.pendencies ");
		jpql.append(" 	     ) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.user u, ");
		jpql.append("        Health h ");
		jpql.append("  where u = h.user ");
		jpql.append("    and u.deleted is null ");
		jpql.append("    and u.email = :email");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("email", email);

		User result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public User loadBasics(Long id) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	new User(u.id, u.email, p.name, p.gender) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.user u ");
		jpql.append("  where u.deleted is null ");
		jpql.append("    and u.id = :id");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("id", id);

		User result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public User loadBasics(String email) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	new User(u.id, u.email, p.name, p.gender) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.user u ");
		jpql.append("  where u.deleted is null ");
		jpql.append("    and u.email = :email");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("email", email);

		User result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	public User load(String email) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("   from User u ");
		jpql.append("  where u.email = :email ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("email", email);

		User result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}
}
