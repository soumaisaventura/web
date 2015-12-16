package adventure.rest;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.business.MailBusiness;
import adventure.entity.User;
import adventure.persistence.UserDAO;
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
		MailBusiness.getInstance().sendResetPasswordMail(data.email.trim().toLowerCase(), baseUri);
	}

	@POST
	@Transactional
	@ValidatePayload
	@Path("reset/{token}")
	@Consumes("application/json")
	@Produces("application/json")
	public User reset(@PathParam("token") String token, PerformResetData data, @Context UriInfo uriInfo)
			throws Exception {
		UserDAO dao = UserDAO.getInstance();
		User persisted = dao.load(data.email.trim().toLowerCase());

		if (persisted == null || !Passwords.hash(token, persisted.getEmail()).equals(persisted.getPasswordResetToken())) {
			throw new UnprocessableEntityException().addViolation("Esta solicitação não é mais válida.");

		} else {
			persisted.setPasswordResetToken(null);
			persisted.setPasswordResetRequest(null);
			persisted.setPassword(Passwords.hash(data.newPassword.trim(), persisted.getEmail()));

			boolean wasActivated = false;
			if (persisted.getActivation() == null) {
				persisted.setActivation(new Date());
				persisted.setActivationToken(null);
				wasActivated = true;
			}

			dao.update(persisted);
			login(persisted.getEmail(), data.newPassword.trim());

			if (wasActivated) {
				URI baseUri = uriInfo.getBaseUri().resolve("..");
				MailBusiness.getInstance().sendWelcome(User.getLoggedIn(), baseUri);
			}
		}

		return User.getLoggedIn();
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
