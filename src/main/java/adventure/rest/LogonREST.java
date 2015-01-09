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
import adventure.persistence.AccountDAO;
import adventure.persistence.MailDAO;
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
	private AccountDAO accountDAO;

	@POST
	@ValidatePayload
	@Consumes("application/json")
	public void login(CredentialsData data, @Context UriInfo uriInfo) throws Exception {
		validate(data, uriInfo);

		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(data.username);
		credentials.setPassword(data.password);

		securityContext.login();
	}

	private void validate(CredentialsData data, UriInfo uriInfo) throws Exception {
		Account persistedAccount = accountDAO.load(data.username);

		if (persistedAccount != null) {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			MailDAO mailDAO = Beans.getReference(MailDAO.class);

			if (persistedAccount.getPassword() == null) {
				mailDAO.sendPasswordCreationMail(persistedAccount.getEmail(), baseUri);

				throw new UnprocessableEntityException()
						.addViolation("Você ainda não definiu uma senha para a sua conta. Siga as instruções no seu e-mail.");
			}

			if (persistedAccount.getConfirmation() == null) {
				mailDAO.sendAccountActivationMail(persistedAccount.getEmail(), baseUri);

				throw new UnprocessableEntityException()
						.addViolation("Sua conta ainda não foi ativada. Siga as instruções no seu e-mail. ");
			}
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
