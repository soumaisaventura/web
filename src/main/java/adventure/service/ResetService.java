package adventure.service;

import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.infinispan.Cache;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.User;
import adventure.persistence.ContainerResources;
import adventure.persistence.MailDAO;
import adventure.persistence.UserDAO;
import adventure.security.Passwords;
import adventure.validator.ExistentUserEmail;
import br.gov.frameworkdemoiselle.resteasy.util.ValidationException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@ValidateRequest
@Path("/api/reset")
@Produces(APPLICATION_JSON)
public class ResetService {

	@POST
	public void requestPasswordReset(@NotNull @Valid ResetRequestForm form) throws MessagingException {
		Beans.getReference(MailDAO.class).sendResetPasswordMail(form.getEmail());
	}

	@POST
	@Transactional
	@Path("/{token}")
	public Response performPasswordReset(@NotEmpty @PathParam("token") String token,
			@NotNull @Valid ResetPerformForm form) throws MessagingException {
		Response response = null;

		Cache<String, String> cache = Beans.getReference(ContainerResources.class).getPasswordResetCache();
		String cachedToken = cache.get(form.getEmail());

		if (cachedToken == null || !cachedToken.equals(token)) {
			Beans.getReference(MailDAO.class).sendResetPasswordMail(form.getEmail());

			ValidationException validation = new ValidationException();
			validation.addViolation("global",
					"Esta solicitação não é mais válida. Siga as instruções que acabamos de enviar para o seu e-mail.");

			response = Response.status(SC_PRECONDITION_FAILED).entity(validation.getConstraintViolations()).build();

		} else {
			cache.remove(form.getEmail());

			UserDAO dao = Beans.getReference(UserDAO.class);
			User persistedUser = dao.loadByEmail(form.getEmail());
			persistedUser.setPassword(Passwords.hash(form.getNewPassword()));
			dao.update(persistedUser);

			login(form.getEmail(), form.getNewPassword());
			response = Response.ok().build();
		}

		return response;
	}

	private void login(String email, String password) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(email);
		credentials.setPassword(password);

		Beans.getReference(SecurityContext.class).login();
	}

	public static class ResetRequestForm {

		@Email
		@NotEmpty
		@ExistentUserEmail
		private String email;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	}

	public static class ResetPerformForm {

		@Email
		@NotEmpty
		@ExistentUserEmail
		private String email;

		@NotEmpty
		private String newPassword;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}
	}
}
