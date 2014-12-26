package br.com.fbca.rest;

import java.io.IOException;
import java.lang.reflect.Field;

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
			dao.insert(oauthUser);
			login(oauthUser);

		} else {
			updateInfo(oauthUser, persistedUser);
			login(oauthUser);
		}
	}

	private void updateInfo(User from, User to) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : Reflections.getNonStaticFields(to.getClass())) {
			if (Reflections.getFieldValue(field, to) == null) {
				Object value = Reflections.getFieldValue(field, from);
				Reflections.setFieldValue(field, to, value);
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
