package adventure.rest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.template.Crud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

public abstract class OAuthLogon {

	@Inject
	private AccountDAO accountDAO;

	@Inject
	private ProfileDAO profileDAO;

	protected abstract Profile createProfile(String code) throws Exception;

	@POST
	@Transactional
	@ValidatePayload
	public void login(CredentialsData data) throws Exception {
		Profile oauthProfile = createProfile(data.token);
		Account oauthAccount = oauthProfile.getAccount();
		Account persistedAccount = accountDAO.load(oauthAccount.getEmail());

		if (persistedAccount == null) {
			oauthAccount.setConfirmation(new Date());
			accountDAO.insert(oauthAccount);
			profileDAO.insert(oauthProfile);
			Beans.getReference(HealthDAO.class).insert(new Health(oauthAccount));

			login(oauthAccount);

		} else if (persistedAccount.getConfirmation() == null) {
			persistedAccount.setConfirmation(new Date());
		}

		if (persistedAccount != null) {
			Profile persistedProfile = profileDAO.load(persistedAccount);
			updateInfo(oauthProfile, persistedProfile, profileDAO);

			updateInfo(oauthAccount, persistedAccount, accountDAO);
			login(oauthAccount);
		}
	}

	private <T> void updateInfo(T from, T to, Crud<T, ?> crud) throws Exception {
		for (Field field : Reflections.getNonStaticFields(to.getClass())) {
			if (Reflections.getFieldValue(field, from) != null && Reflections.getFieldValue(field, to) == null) {
				Object value = Reflections.getFieldValue(field, from);

				String setter = "set" + Strings.firstToUpper(field.getName());
				Method method = to.getClass().getMethod(setter, value.getClass());
				method.invoke(to, value);
			}
		}

		crud.update(to);
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
