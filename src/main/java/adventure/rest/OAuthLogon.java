package adventure.rest;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.POST;

import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Account;
import adventure.entity.Health;
import adventure.entity.Profile;
import adventure.persistence.AccountDAO;
import adventure.persistence.HealthDAO;
import adventure.persistence.ProfileDAO;
import adventure.security.OAuthSession;
import adventure.util.Misc;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

public abstract class OAuthLogon {

	@Inject
	private AccountDAO accountDAO;

	@Inject
	private ProfileDAO profileDAO;

	protected abstract Account createAccount(String code) throws Exception;

	@POST
	@Transactional
	@ValidatePayload
	public void login(CredentialsData data) throws Exception {
		Account oauthAccount = createAccount(data.token);
		Account persistedAccount = accountDAO.loadFull(oauthAccount.getEmail());

		if (persistedAccount == null) {
			oauthAccount.setConfirmation(new Date());
			accountDAO.insert(oauthAccount);

			Profile oauthProfile = oauthAccount.getProfile();
			oauthProfile.setAccount(oauthAccount);
			profileDAO.insert(oauthProfile);

			Beans.getReference(HealthDAO.class).insert(new Health(oauthAccount));
			login(oauthAccount);

		} else if (persistedAccount.getConfirmation() == null) {
			persistedAccount.setConfirmation(new Date());
		}

		if (persistedAccount != null) {
			Profile persistedProfile = persistedAccount.getProfile();

			profileDAO.update(Misc.copyFields(oauthAccount.getProfile(), persistedProfile));
			accountDAO.update(Misc.copyFields(oauthAccount, persistedAccount));

			login(oauthAccount);
		}
	}

	protected void login(Account user) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(user.getEmail());

		Beans.getReference(OAuthSession.class).activate();
		Beans.getReference(SecurityContext.class).login();
	}

	public static class CredentialsData {

		@NotEmpty
		public String token;
	}
}
