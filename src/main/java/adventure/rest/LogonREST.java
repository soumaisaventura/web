package adventure.rest;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Account;
import adventure.persistence.MailDAO;
import adventure.persistence.AccountDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("logon")
public class LogonREST {

	@Inject
	private SecurityContext securityContext;

	@Inject
	private AccountDAO userDAO;

	@POST
	@ValidatePayload
	@Consumes("application/json")
	public void login(CredentialsData data, @Context UriInfo uriInfo) throws Exception {
		Account persistedUser = userDAO.load(data.username);

		if (persistedUser != null && persistedUser.getPassword() == null) {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			Beans.getReference(MailDAO.class).sendPasswordCreationMail(persistedUser.getEmail(), baseUri);

			throw new UnprocessableEntityException()
					.addViolation("Você ainda não definiu uma senha para a sua conta. Siga as instruções no e-mail que acabamos de enviar para você.");

		} else {
			Credentials credentials = Beans.getReference(Credentials.class);
			credentials.setUsername(data.username);
			credentials.setPassword(data.password);

			securityContext.login();
		}
	}

	public static class CredentialsData {

		@Email
		@NotEmpty
		public String username;

		@NotEmpty
		public String password;
	}
}
