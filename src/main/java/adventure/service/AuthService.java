package adventure.service;

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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.User;
import adventure.persistence.MailDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.resteasy.util.ValidationException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
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
			Beans.getReference(MailDAO.class).sendResetPasswordMail(persistedUser.getEmail());

			ValidationException validation = new ValidationException();
			validation
					.addViolation("global",
							"Você ainda não definiu uma senha para a sua conta. Siga as instruções no e-mail que acabamos de enviar para você.");

			response = Response.status(SC_PRECONDITION_FAILED).entity(validation.getConstraintViolations()).build();

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

	private void login(String email, String password) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(email);
		credentials.setPassword(password);

		Beans.getReference(SecurityContext.class).login();
	}

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
