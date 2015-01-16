package adventure.persistence;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Profile;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class ProfileDAO extends JPACrud<Profile, User> {

	private static final long serialVersionUID = 1L;

	@Override
	public Profile insert(Profile profile) {
		if (profile.getName() != null) {
			profile.setName(profile.getName().trim());
		}

		return super.insert(profile);
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
