package adventure.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.User;
import adventure.util.ApplicationConfig;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("logon")
public class LogonREST {

	@POST
	@ValidatePayload
	@Consumes("application/json")
	@Produces("application/json")
	public User login(CredentialsData data, @Context UriInfo uriInfo) throws Exception {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(data.username != null ? data.username.trim().toLowerCase() : null);
		credentials.setPassword(data.password != null ? data.password.trim() : null);

		SecurityContext securityContext = Beans.getReference(SecurityContext.class);
		securityContext.login();

		User user = User.getLoggedIn();
		return user;
	}

	@GET
	@Path("oauth")
	@Produces("application/json")
	public AppIdData getAppIds() {
		ApplicationConfig config = Beans.getReference(ApplicationConfig.class);

		AppIdData data = new AppIdData();
		data.facebook = config.getOAuthFacebookId();
		data.google = config.getOAuthGoogleId();

		return data;
	}

	public static class CredentialsData {

		@Email
		@NotEmpty
		public String username;

		@NotEmpty
		public String password;
	}

	public static class AppIdData {

		public String facebook;

		public String google;
	}
}
