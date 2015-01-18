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

	public List<User> searchAvailable(Race race, String filter, List<Long> excludeIds) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	new User(a.id, a.email, p.name, p.gender) ");
		jpql.append(" from ");
		jpql.append(" 	Profile p join p.user a ");
		jpql.append(" where a.confirmation is not null ");
		jpql.append("   and lower(p.name) like :filter ");
		jpql.append("   and a.id not in :excludeIds ");
		jpql.append("   and a not in ( ");
		jpql.append("       select _a ");
		jpql.append("         from TeamFormation _f ");
		jpql.append("         join _f.registration _r ");
		jpql.append("         join _r.raceCategory _rc ");
		jpql.append("         join _f.user _a ");
		jpql.append("        where _rc.race = :race ");
		jpql.append("          and _f.confirmed = true ) ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setMaxResults(10);

		if (excludeIds.isEmpty()) {
			excludeIds.add(Long.valueOf(-1));
		}

		query.setParameter("excludeIds", excludeIds);
		query.setParameter("filter", Strings.isEmpty(filter) ? "" : "%" + filter.toLowerCase() + "%");
		query.setParameter("race", race);

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
		jpql.append("       a.id, ");
		jpql.append("       a.email, ");
		jpql.append("       a.password, ");
		jpql.append("       a.confirmation, ");
		jpql.append("       a.confirmationToken, ");
		jpql.append("       p.name, ");
		jpql.append("       p.gender) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.user a ");
		jpql.append("  where a.deleted is null ");
		jpql.append("    and a.email = :email");

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
		jpql.append(" 	new User(a.id, a.email, p.name, p.gender) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.user a ");
		jpql.append("  where a.deleted is null ");
		jpql.append("    and a.id = :id");

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
		jpql.append(" 	new User(a.id, a.email, p.name, p.gender) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.user a ");
		jpql.append("  where a.deleted is null ");
		jpql.append("    and a.email = :email");

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
		jpql.append("   from User a ");
		jpql.append("  where a.email = :email ");

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
