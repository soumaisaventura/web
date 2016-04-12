package adventure.persistence;

import adventure.entity.Event;
import adventure.entity.Registration;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Transactional
public class UserDAO extends JPACrud<User, Integer> {

	private static final long serialVersionUID = 1L;

	public static UserDAO getInstance() {
		return Beans.getReference(UserDAO.class);
	}

	public List<User> findOrganizers(Event event) {
		String jpql = "";
		jpql += " select new User ( ";
		jpql += " 	        o.id, ";
		jpql += " 	        case when eo.alternateEmail is null then o.email else eo.alternateEmail end, ";
		jpql += " 	        case when eo.alternateName is null then p.name else eo.alternateName end, ";
		jpql += " 	        p.gender, ";
		jpql += " 	        p.mobile ";
		jpql += " 	     ) ";
		jpql += "   from EventOrganizer eo ";
		jpql += "   join eo.organizer o, ";
		jpql += "        Profile p ";
		jpql += "  where o.id = p.id ";
		jpql += "    and eo.event = :event ";
		jpql += "  order by ";
		jpql += "        p.name ";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
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
		String jpql = "";
		jpql += " select ";
		jpql += " 	 new User( ";
		jpql += " 	     u.id, ";
		jpql += " 	     u.email, ";
		jpql += " 	     p.name, ";
		jpql += " 	     p.gender, ";
		jpql += " 	     p.pendencies, ";
		jpql += " 	     h.pendencies, ";
		jpql += " 	     u.admin, ";
		jpql += " 	     u.organizer ";
		jpql += " 	     ) ";
		jpql += "   from Profile p ";
		jpql += "   join p.user u, ";
		jpql += "        Health h ";
		jpql += "  where u = h.user ";
		jpql += "    and u.activation is not null ";
		jpql += "    and lower(p.name) like :filter ";
		jpql += "    and u.id not in :excludeIds ";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);

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
		String jpql = "";
		jpql += " select ";
		jpql += " 	 new User( ";
		jpql += " 	     u.id, ";
		jpql += " 	     u.email, ";
		jpql += " 	     p.name, ";
		jpql += " 	     p.gender, ";
		jpql += " 	     p.pendencies, ";
		jpql += " 	     h.pendencies, ";
		jpql += " 	     u.admin, ";
		jpql += " 	     u.organizer ";
		jpql += " 	     ) ";
		jpql += "   from Profile p ";
		jpql += "   join p.user u, ";
		jpql += "        Health h ";
		jpql += "  where u = h.user ";
		jpql += "    and lower(p.name) like :name ";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
		query.setParameter("name", name.toLowerCase());

		return query.getResultList();
	}

	public List<User> findDuplicatesByName() {
		String jpql = "";
		jpql += " select distinct ";
		jpql += " 	 new User( ";
		jpql += " 	     u.id, ";
		jpql += " 	     u.email, ";
		jpql += " 	     p.name, ";
		jpql += " 	     p.pendencies + h.pendencies, ";
		jpql += " 	     u.creation, ";
		jpql += " 	     case when u.activation is null then to_date ('1900-01-01', 'YYYY-MM-DD') else u.activation end ";
		jpql += " 	     ) ";
		jpql += "   from Profile p ";
		jpql += "   join p.user u, ";
		jpql += "        Health h, ";
		jpql += "        Profile p2 ";
		jpql += "  where u = h.user ";
		jpql += "    and p.name = p2.name ";
		jpql += "    and p.id <> p2.id ";
		jpql += "    and p.pendencies + h.pendencies > 0";
		jpql += "    and not exists ";
		jpql += "        (select _tf.user.id ";
		jpql += "           from UserRegistration _tf ";
		jpql += "          where _tf.user = u) ";
		jpql += "    and not exists ";
		jpql += "        (select _eo.organizer.id ";
		jpql += "           from EventOrganizer _eo ";
		jpql += "          where _eo.organizer = u) ";
		jpql += "  order by p.name, ";
		jpql += "        (p.pendencies + h.pendencies) desc, ";
		jpql += "        case when u.activation is null then to_date ('1900-01-01', 'YYYY-MM-DD') else u.activation end, ";
		jpql += "        u.creation ";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
		return query.getResultList();
	}

	public List<User> findUserRegistrations(Registration registration) {
		String jpql = "";
		jpql += " select ";
		jpql += " 	 new User( ";
		jpql += " 	     us.id, ";
		jpql += " 	     us.email, ";
		jpql += " 	     p.name, ";
		jpql += " 	     p.gender, ";
		jpql += " 	     p.mobile ";
		jpql += " 	     ) ";
		jpql += "   from UserRegistration tf ";
		jpql += "   join tf.user us, ";
		jpql += "        Profile p ";
		jpql += "  where us = p.user ";
		jpql += "    and tf.registration = :registration ";
		jpql += "  order by ";
		jpql += "        p.name ";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
		query.setParameter("registration", registration);

		return query.getResultList();
	}

	public User loadForAuthentication(String email) {
		String jpql = "";
		jpql += " select ";
		jpql += "    new User( ";
		jpql += "        u.id, ";
		jpql += "        u.email, ";
		jpql += "        u.password, ";
		jpql += "        u.activation, ";
		jpql += "        u.activationToken, ";
		jpql += "        u.passwordResetRequest, ";
		jpql += "        u.passwordResetToken, ";
		jpql += "        p.name, ";
		jpql += "        p.gender, ";
		jpql += " 	     p.pendencies, ";
		jpql += " 	     h.pendencies, ";
		jpql += " 	     u.admin, ";
		jpql += " 	     u.organizer ";
		jpql += " 	     ) ";
		jpql += "   from Profile p";
		jpql += "   join p.user u, ";
		jpql += "        Health h ";
		jpql += "  where u = h.user ";
		jpql += "    and u.deleted is null ";
		jpql += "    and u.email = :email";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
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
		String jpql = "";
		jpql += " select ";
		jpql += " 	 new User( ";
		jpql += " 	     u.id, ";
		jpql += " 	     u.email, ";
		jpql += " 	     p.name, ";
		jpql += " 	     p.gender, ";
		jpql += " 	     p.pendencies, ";
		jpql += " 	     h.pendencies, ";
		jpql += " 	     u.admin, ";
		jpql += " 	     u.organizer ";
		jpql += " 	     ) ";
		jpql += "   from Profile p";
		jpql += "   join p.user u, ";
		jpql += "        Health h ";
		jpql += "  where u = h.user ";
		jpql += "    and u.deleted is null ";
		jpql += "    and u.id = :id";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
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
		String jpql = "";
		jpql += " select ";
		jpql += " 	 new User( ";
		jpql += " 	     u.id, ";
		jpql += " 	     u.email, ";
		jpql += " 	     p.name, ";
		jpql += " 	     p.gender, ";
		jpql += " 	     p.pendencies, ";
		jpql += " 	     h.pendencies, ";
		jpql += " 	     u.admin, ";
		jpql += " 	     u.organizer ";
		jpql += " 	     ) ";
		jpql += "   from Profile p";
		jpql += "   join p.user u, ";
		jpql += "        Health h ";
		jpql += "  where u = h.user ";
		jpql += "    and u.deleted is null ";
		jpql += "    and u.email = :email";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
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
		String jpql = "";
		jpql += "   from User u ";
		jpql += "  where u.email = :email ";

		TypedQuery<User> query = getEntityManager().createQuery(jpql, User.class);
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
