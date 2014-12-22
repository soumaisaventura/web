package br.com.fbca.rest;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.infinispan.Cache;

import br.com.fbca.entity.User;
import br.com.fbca.persistence.ContainerResources;
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
	public void requestPasswordReset(ResetRequestData data) throws MessagingException {
		Beans.getReference(MailDAO.class).sendResetPasswordMail(data.email);
	}

	@POST
	@Transactional
	@ValidatePayload
	@Path("/{token}")
	@Consumes("application/json")
	public void performPasswordReset(@PathParam("token") String token, ResetPerformData data) throws Exception {
		Cache<String, String> cache = Beans.getReference(ContainerResources.class).getPasswordResetCache();
		String cachedToken = cache.get(data.email);

		if (cachedToken == null || !cachedToken.equals(token)) {
			Beans.getReference(MailDAO.class).sendResetPasswordMail(data.email);

			throw new UnprocessableEntityException()
					.addViolation("Esta solicitação não é mais válida. Siga as instruções que acabamos de enviar para o seu e-mail.");

		} else {
			cache.remove(data.email);

			UserDAO dao = Beans.getReference(UserDAO.class);
			User persistedUser = dao.loadByEmail(data.email);
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

	public static class ResetRequestData {

		@Email
		@NotEmpty
		@ExistentUserEmail
		String email;
	}

	public static class ResetPerformData {

		@Email
		@NotEmpty
		@ExistentUserEmail
		String email;

		@NotEmpty
		String newPassword;
	}
}
