package adventure.persistence;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Profile;
import adventure.entity.User;
import adventure.util.PendencyCounter;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class ProfileDAO extends JPACrud<Profile, User> {

	private static final long serialVersionUID = 1L;

	public static ProfileDAO getInstance() {
		return Beans.getReference(ProfileDAO.class);
	}

	@Override
	public Profile insert(Profile profile) {
		if (profile.getName() != null) {
			profile.setName(profile.getName().trim());
		}

		profile.setPendencies(PendencyCounter.count(profile));
		return super.insert(profile);
	}

	@Override
	public Profile update(Profile profile) {
		profile.setPendencies(PendencyCounter.count(profile));
		return super.update(profile);
	}

	public Profile loadDetails(User user) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	 new Profile( ");
		jpql.append(" 	     p.name, ");
		jpql.append(" 	     p.rg, ");
		jpql.append(" 	     p.cpf, ");
		jpql.append(" 	     p.birthday, ");
		jpql.append(" 	     p.mobile, ");
		jpql.append(" 	     p.gender, ");
		jpql.append(" 	     p.pendencies, ");
		jpql.append(" 	     u.id, ");
		jpql.append(" 	     u.email, ");
		jpql.append(" 	     c.id, ");
		jpql.append(" 	     c.name, ");
		jpql.append(" 	     s.id, ");
		jpql.append(" 	     s.name, ");
		jpql.append(" 	     s.abbreviation ");
		jpql.append(" 	 ) ");
		jpql.append("   from Profile p ");
		jpql.append("   join p.user u ");
		jpql.append("   left join p.city c ");
		jpql.append("   left join c.state s ");
		jpql.append("  where p.user = :user ");

		TypedQuery<Profile> query = getEntityManager().createQuery(jpql.toString(), Profile.class);
		query.setParameter("user", user);

		Profile result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}

	@Override
	public Profile load(User user) {
		String jpql = "from Profile p where p.user = :user";
		TypedQuery<Profile> query = getEntityManager().createQuery(jpql, Profile.class);
		query.setParameter("user", user);

		Profile result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}
}
