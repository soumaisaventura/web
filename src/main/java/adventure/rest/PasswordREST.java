package adventure.rest;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Account;
import adventure.persistence.AccountDAO;
import adventure.persistence.MailDAO;
import adventure.security.Passwords;
import adventure.validator.ExistentUserEmail;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("password")
public class PasswordREST {

	@POST
	@ValidatePayload
	@Path("recovery")
	@Consumes("application/json")
	public void recovery(RecoveryData data, @Context UriInfo uriInfo) throws Exception {
		URI baseUri = uriInfo.getBaseUri().resolve("..");
		Beans.getReference(MailDAO.class).sendResetPasswordMail(data.email, baseUri);
	}

	@POST
	@Transactional
	@ValidatePayload
	@Path("reset/{token}")
	@Consumes("application/json")
	public void reset(@PathParam("token") String token, PerformResetData data) throws Exception {
		AccountDAO dao = Beans.getReference(AccountDAO.class);
		Account account = dao.loadFull(data.email);

		if (account == null || !Passwords.hash(token, account.getEmail()).equals(account.getPasswordResetToken())) {
			throw new UnprocessableEntityException().addViolation("Esta solicitação não é mais válida.");

		} else {
			account.setPasswordResetToken(null);
			account.setPasswordResetRequest(null);
			account.setPassword(Passwords.hash(data.newPassword, account.getEmail()));
			account.setConfirmation(new Date());
			account.setConfirmationToken(null);
			dao.update(account);

			login(data.email, data.newPassword);
		}
	}

	private void login(String email, String password) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(email);
		credentials.setPassword(password);

		Beans.getReference(SecurityContext.class).login();
	}

	public static class RecoveryData {

		@Email
		@NotEmpty
		@ExistentUserEmail
		public String email;
	}

	public static class PerformResetData {

		@Email
		@NotEmpty
		public String email;

		@NotEmpty
		public String newPassword;
	}
}
