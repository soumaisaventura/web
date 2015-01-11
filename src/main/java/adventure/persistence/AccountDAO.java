package adventure.persistence;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.Account;
import adventure.entity.Gender;
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

	public Account loadForAuthentication(String email) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append("    new " + LoadForAuthentication.class.getName() + "( ");
		jpql.append("       a.id, ");
		jpql.append("       a.email, ");
		jpql.append("       a.password, ");
		jpql.append("       a.confirmation, ");
		jpql.append("       a.confirmationToken, ");
		jpql.append("       p.name, ");
		jpql.append("       p.gender) ");
		jpql.append("   from Profile p");
		jpql.append("   join p.account a ");
		jpql.append("  where a.deleted is null ");
		jpql.append("    and a.email = :email");

		TypedQuery<Account> query = getEntityManager().createQuery(jpql.toString(), Account.class);
		query.setParameter("email", email);

		Account result;

		try {
			result = query.getSingleResult();
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

	public static class Load {

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

	public static class LoadForAuthentication extends Account {

		private static final long serialVersionUID = 1L;

		public LoadForAuthentication(Long id, String email, String password, Date confirmation,
				String confirmationToken, String name, Gender gender) throws Exception {
			setId(id);
			setEmail(email);
			setPassword(password);
			setConfirmation(confirmation);
			setConfirmationToken(confirmationToken);

			setProfile(new Profile());
			getProfile().setName(name);
			getProfile().setGender(gender);
		}
	}

	public Account load(String email) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("   from Account a ");
		jpql.append("  where a.deleted is null ");
		jpql.append("    and a.email = :email ");

		TypedQuery<Account> query = getEntityManager().createQuery(jpql.toString(), Account.class);
		query.setParameter("email", email);

		Account result;

		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}

		return result;
	}
}
