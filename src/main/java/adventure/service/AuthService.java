package adventure.service;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.infinispan.Cache;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.JSEntity;
import adventure.entity.User;
import adventure.persistence.ContainerResources;
import adventure.persistence.MailDAO;
import adventure.persistence.UserDAO;
import adventure.security.Passwords;
import adventure.validator.ExistentUserEmail;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@ValidateRequest
@Path("/api/auth")
@Produces(APPLICATION_JSON)
public class AuthService {

	@Inject
	private SecurityContext securityContext;

	@POST
	public Response login(@NotNull @Valid CredentialsForm form) throws MessagingException, IllegalAccessException,
			InvocationTargetException {
		Response response = null;
		User persistedUser = Beans.getReference(UserDAO.class).loadByEmail(form.getUsername());

		if (persistedUser != null && persistedUser.getPassword() == null) {
			sendResetPasswordMail(persistedUser.getEmail());

			String message = "Você ainda não definiu uma senha para a sua conta. Siga as instruções no e-mail que acabamos de enviar para você.";
			response = Response.status(SC_FORBIDDEN).entity(message).build();

		} else {
			login(form.getUsername(), form.getPassword());
			response = Response.ok().build();
		}

		return response;
	}

	@DELETE
	@LoggedIn
	public void logout() {
		try {
			securityContext.logout();
		} catch (NullPointerException cause) {
			// Abafando excendo para corrigir um erro interno do Demoiselle
		}
	}

	@GET
	@LoggedIn
	public User getAuthenticatedUser() {
		return new User(securityContext.getUser());
	}

	@POST
	@Path("/reset")
	public void requestPasswordReset(@NotNull @Valid ResetRequestForm form) throws MessagingException {
		sendResetPasswordMail(form.getEmail());
	}

	@POST
	@Transactional
	@Path("/reset/{token}")
	public Response performPasswordReset(@NotEmpty @PathParam("token") String token,
			@NotNull @Valid ResetPerformForm form) throws MessagingException {
		Response response = null;

		Cache<String, String> cache = Beans.getReference(ContainerResources.class).getPasswordResetCache();
		String cachedToken = cache.get(form.getEmail());

		if (cachedToken == null || !cachedToken.equals(token)) {
			sendResetPasswordMail(form.getEmail());

			// TODO Informar ao usuário que a solicitação é inválida e que uma
			// nova mensagem foi enviada para seu
			// e-mail.
			// Solicitar para que ele siga as instruções enviados por e-mail.
			String message = "ops...";
			response = Response.status(SC_PRECONDITION_FAILED).entity(message).build();

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

	private void sendResetPasswordMail(String email) throws MessagingException {
		Cache<String, String> cache = Beans.getReference(ContainerResources.class).getPasswordResetCache();
		String token = cache.get(email);

		if (token == null) {
			token = Passwords.randomToken();
			cache.put(email, token);
		}

		Beans.getReference(MailDAO.class).sendResetPasswordMail(email, token);
	}

	@JSEntity
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

	@JSEntity
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

	@JSEntity
	public static class CredentialsForm {

		@Email
		@NotEmpty
		private String username;

		@NotEmpty
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
