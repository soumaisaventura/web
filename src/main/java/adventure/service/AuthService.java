package adventure.service;

import java.security.Principal;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.User;
import adventure.persistence.MailDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("auth")
public class AuthService {

	@Inject
	private SecurityContext securityContext;

	@POST
	@ValidatePayload
	@Consumes("application/json")
	public void login(CredentialsData data) throws Exception {
		User persistedUser = Beans.getReference(UserDAO.class).loadByEmail(data.username);

		if (persistedUser != null && persistedUser.getPassword() == null) {
			Beans.getReference(MailDAO.class).sendResetPasswordMail(persistedUser.getEmail());

			throw new UnprocessableEntityException()
					.addViolation("global",
							"Você ainda não definiu uma senha para a sua conta. Siga as instruções no e-mail que acabamos de enviar para você.");

		} else {
			login(data.username, data.password);
		}
	}

	@DELETE
	@LoggedIn
	public void logout() {
		securityContext.logout();
	}

	@GET
	@LoggedIn
	@Produces("application/json")
	public Principal getAuthenticatedUser() {
		return securityContext.getUser();
	}

	private void login(String email, String password) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(email);
		credentials.setPassword(password);

		securityContext.login();
	}

	public static class CredentialsData {

		@Email
		@NotEmpty
		String username;

		@NotEmpty
		String password;
	}
}
