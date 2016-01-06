package adventure.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Event;
import adventure.entity.Registration;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

@Transactional
public class UserDAO extends JPACrud<User, Integer> {

	private static final long serialVersionUID = 1L;

	public static UserDAO getInstance() {
		return Beans.getReference(UserDAO.class);
	}

	public List<User> findOrganizers(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select new User ( ");
		jpql.append(" 	        o.id, ");
		jpql.append(" 	        case when eo.alternateEmail is null then o.email else eo.alternateEmail end, ");
		jpql.append(" 	        case when eo.alternateName is null then p.name else eo.alternateName end, ");
		jpql.append(" 	        p.gender, ");
		jpql.append(" 	        p.mobile ");
		jpql.append(" 	     ) ");
		jpql.append("   from EventOrganizer eo ");
		jpql.append("   join eo.organizer o, ");
		jpql.append("        Profile p ");
		jpql.append("  where o.id = p.id ");
		jpql.append("    and eo.event = :event ");
		jpql.append("  order by ");
		jpql.append("        p.name ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("event", event);

		return query.getResultList();
	}

	// TODO: OLD

	@Override
	public User insert(User user) {
		user.setCreation(new Date());

		if (user.getEmail() != null) {
			user.setEmail(user.getEmail().trim().toLowerCase());
		}

		if (user.getAdmin() == null) {
			user.setAdmin(false);
		}

		if (user.getOrganizer() == null) {
			user.setOrganizer(false);
		}

		return super.insert(user);
	}

	@Override
	public User update(User entity) {
		return super.update(entity);
	}

	public List<User> search(String filter, List<Integer> excludeIds) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new User( ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.gender, ");
		jpql.append(" 	     p.pendencies, ");
		jpql.append(" 	     h.pendencies, ");
		jpql.append(" 	     u.admin, ");
		jpql.append(" 	     u.organizer ");
		jpql.append(" 	     ) ");
		jpql.append("   from Profile p ");
		jpql.append("   join p.user u, ");
		jpql.append("        Health h ");
		jpql.append("  where u = h.user ");
		jpql.append("    and u.activation is not null ");
		jpql.append("    and lower(p.name) like :filter ");
		jpql.append("    and u.id not in :excludeIds ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);

		if (!"%".equals(filter)) {
			query.setMaxResults(10);
		}

		if (excludeIds.isEmpty()) {
			excludeIds.add(-1);
		}

		query.setParameter("excludeIds", excludeIds);
		query.setParameter("filter", Strings.isEmpty(filter) ? "" : "%" + filter.toLowerCase() + "%");

		return query.getResultList();
	}

	public List<User> findByName(String name) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new User( ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.gender, ");
		jpql.append(" 	     p.pendencies, ");
		jpql.append(" 	     h.pendencies, ");
		jpql.append(" 	     u.admin, ");
		jpql.append(" 	     u..organizer ");
		jpql.append(" 	     ) ");
		jpql.append("   from Profile p ");
		jpql.append("   join p.user u, ");
		jpql.append("        Health h ");
		jpql.append("  where u = h.user ");
		jpql.append("    and lower(p.name) like :name ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("name", name.toLowerCase());

		return query.getResultList();
	}

	public List<User> findDuplicatesByName() {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select distinct ");
		jpql.append(" 	 new User( ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.pendencies + h.pendencies, ");
		jpql.append(" 	     u.creation, ");
		jpql.append(" 	     case when u.activation is null then to_date ('1900-01-01', 'YYYY-MM-DD') else u.activation end ");
		jpql.append(" 	     ) ");
		jpql.append("   from Profile p ");
		jpql.append("   join p.user u, ");
		jpql.append("        Health h, ");
		jpql.append("        Profile p2 ");
		jpql.append("  where u = h.user ");
		jpql.append("    and p.name = p2.name ");
		jpql.append("    and p.id <> p2.id ");
		jpql.append("    and p.pendencies + h.pendencies > 0");
		jpql.append("    and not exists ");
		jpql.append("        (select _tf.user.id ");
		jpql.append("           from UserRegistration _tf ");
		jpql.append("          where _tf.user = u) ");
		jpql.append("    and not exists ");
		jpql.append("        (select _eo.organizer.id ");
		jpql.append("           from EventOrganizer _eo ");
		jpql.append("          where _eo.organizer = u) ");
		jpql.append("  order by p.name, ");
		jpql.append("        (p.pendencies + h.pendencies) desc, ");
		jpql.append("        case when u.activation is null then to_date ('1900-01-01', 'YYYY-MM-DD') else u.activation end, ");
		jpql.append("        u.creation ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		return query.getResultList();
	}

	public List<User> findUserRegistrations(Registration registration) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new User( ");
		jpql.append(" 	     us.id, ");
		jpql.append(" 	     us.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.gender, ");
		jpql.append(" 	     p.mobile ");
		jpql.append(" 	     ) ");
		jpql.append("   from UserRegistration tf ");
		jpql.append("   join tf.user us, ");
		jpql.append("        Profile p ");
		jpql.append("  where us = p.user ");
		jpql.append("    and tf.registration = :registration ");
		jpql.append("  order by ");
		jpql.append("        p.name ");

		TypedQuery<User> query = getEntityManager().createQuery(jpql.toString(), User.class);
		query.setParameter("registration", registration);

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
		jpql.append("        u.passwordResetRequest, ");
		jpql.append("        u.passwordResetToken, ");
		jpql.append("        p.name, ");
		jpql.append("        p.gender, ");
		jpql.append(" 	     p.pendencies, ");
		jpql.append(" 	     h.pendencies, ");
		jpql.append(" 	     u.admin, ");
		jpql.append(" 	     u.organizer ");
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

	public User loadBasics(Integer id) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new User( ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.gender, ");
		jpql.append(" 	     p.pendencies, ");
		jpql.append(" 	     h.pendencies, ");
		jpql.append(" 	     u.admin, ");
		jpql.append(" 	     u.organizer ");
		jpql.append(" 	     ) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.user u, ");
		jpql.append("        Health h ");
		jpql.append("  where u = h.user ");
		jpql.append("    and u.deleted is null ");
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
		jpql.append(" 	 new User( ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.gender, ");
		jpql.append(" 	     p.pendencies, ");
		jpql.append(" 	     h.pendencies, ");
		jpql.append(" 	     u.admin, ");
		jpql.append(" 	     u.organizer ");
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
