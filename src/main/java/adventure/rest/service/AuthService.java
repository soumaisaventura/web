package adventure.rest.service;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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

import adventure.entity.User;
import adventure.persistence.ContainerResources;
import adventure.persistence.MailDAO;
import adventure.persistence.UserDAO;
import adventure.security.Credentials;
import adventure.security.Passwords;
import adventure.validator.ExistentUserEmail;
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
	public Response login(@NotNull @Valid Credentials credentials) throws MessagingException, IllegalAccessException,
			InvocationTargetException {
		Response response = null;
		User persistedUser = Beans.getReference(UserDAO.class).loadByEmail(credentials.getEmail());

		if (persistedUser != null && persistedUser.getPassword() == null) {
			sendResetPasswordMail(persistedUser.getEmail());

			String message = "Você ainda não definiu uma senha para a sua conta. Siga as instruções no e-mail que acabamos de enviar para você.";
			response = Response.status(SC_FORBIDDEN).entity(message).build();

		} else {
			login(credentials.getEmail(), credentials.getPassword());
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
	public void requestPasswordReset(@NotNull @Valid PasswordReset passwordReset) throws MessagingException {
		sendResetPasswordMail(passwordReset.getEmail());
	}

	@POST
	@Transactional
	@Path("/reset/{token}")
	public Response performPasswordReset(@NotEmpty @PathParam("token") String token,
			@NotEmpty @Email @FormParam("email") String email, @NotEmpty @FormParam("newPassword") String newPassword)
			throws MessagingException {
		Response response = null;

		Cache<String, String> cache = Beans.getReference(ContainerResources.class).getPasswordResetCache();
		String cachedToken = cache.get(email);

		if (cachedToken == null || !cachedToken.equals(token)) {
			sendResetPasswordMail(email);

			// TODO Informar ao usuário que a solicitação é inválida e que uma nova mensagem foi enviada para seu
			// e-mail.
			// Solicitar para que ele siga as instruções enviados por e-mail.
			String message = "ops...";
			response = Response.status(SC_PRECONDITION_FAILED).entity(message).build();

		} else {
			cache.remove(email);

			UserDAO dao = Beans.getReference(UserDAO.class);
			User persistedUser = dao.loadByEmail(email);
			persistedUser.setPassword(Passwords.hash(newPassword));
			dao.update(persistedUser);

			login(email, newPassword);
			response = Response.ok().build();
		}

		return response;
	}

	private void login(String email, String password) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setEmail(email);
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

	static class PasswordReset {

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
}
