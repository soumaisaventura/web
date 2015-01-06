package adventure.persistence;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

import adventure.entity.Profile;
import adventure.entity.Account;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class ProfileDAO extends JPACrud<Profile, Account> {

	private static final long serialVersionUID = 1L;

	@Override
	public Profile load(Account account) {
		String jpql = "from Profile where account = :account";
		TypedQuery<Profile> query = getEntityManager().createQuery(jpql, Profile.class);
		query.setParameter("account", account);

		Profile result;

		try {
			result = query.getSingleResult();
		} catch (EntityNotFoundException cause) {
			result = null;
		}

		return result;
	}
}
