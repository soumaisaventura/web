package adventure.rest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.POST;

import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Profile;
import adventure.entity.Account;
import adventure.persistence.ProfileDAO;
import adventure.persistence.AccountDAO;
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
	private AccountDAO userDAO;

	@Inject
	private ProfileDAO profileDAO;

	protected abstract Profile createProfile(String code) throws IOException;

	@POST
	@Transactional
	@ValidatePayload
	public void login(CredentialsData data) throws Exception {
		Profile oauthProfile = createProfile(data.token);
		Account oauthUser = oauthProfile.getAccount();
		Account persistedUser = userDAO.load(oauthUser.getEmail());

		if (persistedUser == null) {
			oauthUser.setActivation(new Date());
			userDAO.insert(oauthUser);
			profileDAO.insert(oauthProfile);

			login(oauthUser);

		} else if (persistedUser.getActivation() == null) {
			persistedUser.setActivation(new Date());
		}

		if (persistedUser != null) {
			Profile persistedProfile = profileDAO.load(persistedUser);
			updateInfo(oauthProfile, persistedProfile, profileDAO);

			updateInfo(oauthUser, persistedUser, userDAO);
			login(oauthUser);
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
