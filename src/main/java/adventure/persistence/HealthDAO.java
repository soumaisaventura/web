package adventure.persistence;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

import adventure.entity.Account;
import adventure.entity.Health;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class HealthDAO extends JPACrud<Health, Account> {

	private static final long serialVersionUID = 1L;

	public Health load(String email) {
		String jpql = "from Health where account.email = :email";
		TypedQuery<Health> query = getEntityManager().createQuery(jpql, Health.class);
		query.setParameter("email", email);

		Health result;

		try {
			result = query.getSingleResult();
		} catch (EntityNotFoundException cause) {
			result = null;
		}

		return result;
	}

	@Override
	public Health load(Account account) {
		String jpql = "from Health where account = :account";
		TypedQuery<Health> query = getEntityManager().createQuery(jpql, Health.class);
		query.setParameter("account", account);

		Health result;

		try {
			result = query.getSingleResult();
		} catch (EntityNotFoundException cause) {
			result = null;
		}

		return result;
	}
}
