package adventure.rest;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Health;
import adventure.entity.User;
import adventure.persistence.HealthDAO;
import adventure.persistence.MailDAO;
import adventure.persistence.ProfileDAO;
import adventure.persistence.UserDAO;
import adventure.security.OAuthSession;
import adventure.util.Misc;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

public abstract class OAuthLogon {

	protected abstract User createUser(String code) throws Exception;

	@POST
	@Transactional
	@ValidatePayload
	public void login(CredentialsData data, @Context UriInfo uriInfo) throws Exception {
		UserDAO userDAO = UserDAO.getInstance();
		ProfileDAO profileDAO = ProfileDAO.getInstance();

		User oauth = createUser(data.token);
		User persisted = UserDAO.getInstance().loadForAuthentication(oauth.getEmail());

		if (persisted == null) {
			oauth.setConfirmation(new Date());
			userDAO.insert(oauth);
			oauth.getProfile().setUser(oauth);
			profileDAO.insert(oauth.getProfile());
			Beans.getReference(HealthDAO.class).insert(new Health(oauth));

			URI baseUri = uriInfo.getBaseUri().resolve("..");
			login(oauth.getEmail());
			Beans.getReference(MailDAO.class).sendWelcome(User.getLoggedIn(), baseUri);

		} else if (persisted.getConfirmation() == null) {
			oauth.setConfirmation(new Date());
		}

		if (persisted != null) {
			persisted = userDAO.load(persisted.getId());
			Misc.copyFields(oauth, persisted);
			userDAO.update(persisted);

			persisted.setProfile(profileDAO.load(persisted));
			Misc.copyFields(oauth.getProfile(), persisted.getProfile());
			profileDAO.update(persisted.getProfile());

			login(oauth.getEmail());
		}
	}

	protected void login(String username) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(username);

		Beans.getReference(OAuthSession.class).activate();
		Beans.getReference(SecurityContext.class).login();
	}

	public static class CredentialsData {

		@NotEmpty
		public String token;
	}
}
