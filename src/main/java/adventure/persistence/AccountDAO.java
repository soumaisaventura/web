package adventure.persistence;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Account;
import adventure.entity.Health;
import adventure.entity.Profile;
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

	@Override
	public Account load(Long id) {
		return loadFull("id", id, false);
	}

	public Account loadForAuthentication(String email) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new " + Load.class.getName() + "(a, p, h) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.account a, ");
		jpql.append("        Health h ");
		jpql.append("  where h.account = a ");
		jpql.append("    and a.email = :email ");
		jpql.append("   and a.deleted is null");

		TypedQuery<Load> query = getEntityManager().createQuery(jpql.toString(), Load.class);
		query.setParameter("email", email);

		Account result;

		try {
			result = query.getSingleResult().geAccount();
		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}

	private Account loadFull(String field, Object value, boolean includeDeleted) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new " + Load.class.getName() + "(a, p, h) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.account a, ");
		jpql.append("        Health h ");
		jpql.append("  where h.account = a ");
		jpql.append("    and a." + field + " = :value ");

		if (!includeDeleted) {
			jpql.append("   and a.deleted is null");
		}

		TypedQuery<Load> query = getEntityManager().createQuery(jpql.toString(), Load.class);
		query.setParameter("value", value);

		Account result;

		try {
			result = query.getSingleResult().geAccount();
		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}

	public Account loadFull(String email) {
		return loadFull("email", email, false);
	}

	public Account loadFull(String email, boolean includeDeleted) {
		return loadFull("email", email, false);
	}

	public static class Load extends Account {

		private static final long serialVersionUID = 1L;

		private Account account;

		public Load(Account account, Profile profile, Health health) throws Exception {
			account.setProfile(profile);
			account.setHealth(health);
			this.account = account;
		}

		public Account geAccount() {
			return this.account;
		}
	}
}
