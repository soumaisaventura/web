package br.com.fbca.rest;

import java.net.URI;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.fbca.entity.User;
import br.com.fbca.persistence.MailDAO;
import br.com.fbca.persistence.UserDAO;
import br.com.fbca.security.Passwords;
import br.com.fbca.validator.ExistentUserEmail;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("reset")
public class ResetREST {

	@POST
	@ValidatePayload
	@Consumes("application/json")
	public void requestPasswordReset(RequestResetData data, @Context UriInfo uriInfo) throws MessagingException {
		URI baseUri = uriInfo.getBaseUri().resolve("..");
		Beans.getReference(MailDAO.class).sendResetPasswordMail(data.email, baseUri);
	}

	@POST
	@Transactional
	@ValidatePayload
	@Path("/{token}")
	@Consumes("application/json")
	public void performPasswordReset(@PathParam("token") String token, PerformResetData data, @Context UriInfo uriInfo)
			throws Exception {
		UserDAO dao = Beans.getReference(UserDAO.class);
		User persistedUser = dao.loadByEmail(data.email);
		String cachedToken = persistedUser.getPasswordResetToken();

		if (cachedToken == null || !cachedToken.equals(token)) {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			Beans.getReference(MailDAO.class).sendResetPasswordMail(data.email, baseUri);

			throw new UnprocessableEntityException()
					.addViolation("Esta solicitação não é mais válida. Siga as instruções que acabamos de enviar para o seu e-mail.");

		} else {
			persistedUser.setPasswordResetToken(null);
			persistedUser.setPasswordResetRequest(null);
			persistedUser.setPassword(Passwords.hash(data.newPassword));
			dao.update(persistedUser);

			login(data.email, data.newPassword);
		}
	}

	private void login(String email, String password) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(email);
		credentials.setPassword(password);

		Beans.getReference(SecurityContext.class).login();
	}

	public static class RequestResetData {

		@Email
		@NotEmpty
		@ExistentUserEmail
		public String email;
	}

	public static class PerformResetData {

		@Email
		@NotEmpty
		@ExistentUserEmail
		public String email;

		@NotEmpty
		public String newPassword;
	}
}
