package adventure.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("logon")
public class LogonREST {

	@Inject
	private SecurityContext securityContext;

	@POST
	@ValidatePayload
	@Consumes("application/json")
	public void login(CredentialsData data, @Context UriInfo uriInfo) throws Exception {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(data.username);
		credentials.setPassword(data.password);

		securityContext.login();
	}

	@GET
	@Path("appid")
	@Produces("application/json")
	public AppIdData getAppIds() {
		AppIdData data = new AppIdData();

		data.facebook = "1422799641299675";
		data.google = "611475192580-k33ghah4orsl7d4r1r6qml5i4rtgnnrd.apps.googleusercontent.com";

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
