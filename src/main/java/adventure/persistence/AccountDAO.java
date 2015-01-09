package adventure.persistence;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Account;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class AccountDAO extends JPACrud<Account, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Account insert(Account account) {
		account.setCreation(new Date());

		if (account.getEmail() != null) {
			account.setEmail(account.getEmail().trim());
		}

		return super.insert(account);
	}

	@Override
	public void delete(Long id) {
		Account account = load(id);

		if (account != null) {
			account.setDeleted(new Date());
			update(account);
		}
	}

	public Account load(String email, boolean includeDeleted) {
		String jpql = "from Account where email = :email";

		if (!includeDeleted) {
			jpql += " and deleted is null";
		}

		TypedQuery<Account> query = getEntityManager().createQuery(jpql, Account.class);
		query.setParameter("email", email);

		Account result;

		try {
			result = query.getSingleResult();
			// getEntityManager().detach(result);

		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}

	public Account load(String email) {
		return load(email, false);
	}
}
