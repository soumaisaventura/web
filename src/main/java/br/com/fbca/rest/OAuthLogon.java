package br.com.fbca.rest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.POST;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.fbca.entity.User;
import br.com.fbca.persistence.UserDAO;
import br.com.fbca.security.OAuthSession;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

public abstract class OAuthLogon {

	@Inject
	private UserDAO dao;

	protected abstract User getUserInfo(String code) throws IOException;

	@POST
	@Transactional
	@ValidatePayload
	public void login(CredentialsData data) throws Exception {
		User oauthUser = getUserInfo(data.token);
		User persistedUser = dao.loadByEmail(oauthUser.getEmail());

		if (persistedUser == null) {
			oauthUser.setActivation(new Date());
			dao.insert(oauthUser);
			login(oauthUser);

		} else {
			persistedUser.setActivation(new Date());
			updateInfo(oauthUser, persistedUser);
			login(oauthUser);
		}
	}

	private void updateInfo(User from, User to) throws Exception {
		for (Field field : Reflections.getNonStaticFields(to.getClass())) {
			if (Reflections.getFieldValue(field, from) != null && Reflections.getFieldValue(field, to) == null) {
				Object value = Reflections.getFieldValue(field, from);

				String setter = "set" + Strings.firstToUpper(field.getName());
				Method method = to.getClass().getMethod(setter, value.getClass());
				method.invoke(to, value);
			}
		}

		dao.update(to);
	}

	protected void login(User user) {
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
