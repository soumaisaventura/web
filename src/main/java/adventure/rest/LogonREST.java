package adventure.rest;

import adventure.entity.User;
import adventure.rest.data.UserData;
import adventure.util.ApplicationConfig;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("logon")
public class LogonREST {

	@POST
	@ValidatePayload
	@Consumes("application/json")
	@Produces("application/json")
	public UserData login(CredentialsData data, @Context UriInfo uriInfo) throws Exception {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(data.username != null ? data.username.trim().toLowerCase() : null);
		credentials.setPassword(data.password != null ? data.password.trim() : null);

		SecurityContext securityContext = Beans.getReference(SecurityContext.class);
		securityContext.login();

		return new UserData(User.getLoggedIn(), uriInfo);
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
