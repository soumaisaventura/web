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

import adventure.persistence.MailDAO;
import adventure.security.PasswordNotDefinedException;
import adventure.security.UnconfirmedAccountException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.AuthenticationException;
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

		try {
			securityContext.login();
		} catch (AuthenticationException cause) {
			handle(cause, data.username, uriInfo);
		}
	}

	private void handle(AuthenticationException exception, String email, UriInfo uriInfo) throws Exception {
		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailDAO mailDAO = Beans.getReference(MailDAO.class);

		if (exception instanceof PasswordNotDefinedException) {
			mailDAO.sendPasswordCreationMail(email, baseUri);

			throw new UnprocessableEntityException().addViolation(exception.getMessage()
					+ " Siga as instruções no seu e-mail.");

		} else if (exception instanceof UnconfirmedAccountException) {
			mailDAO.sendAccountActivationMail(email, baseUri);

			throw new UnprocessableEntityException().addViolation(exception.getMessage()
					+ " Siga as instruções no seu e-mail. ");
		} else {
			throw exception;
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
