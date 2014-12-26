package br.com.fbca.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.fbca.entity.User;
import br.com.fbca.persistence.MailDAO;
import br.com.fbca.persistence.UserDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
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
	public void login(CredentialsData data) throws Exception {
		User persistedUser = Beans.getReference(UserDAO.class).loadByEmail(data.username);

		if (persistedUser != null && persistedUser.getPassword() == null) {
			Beans.getReference(MailDAO.class).sendResetPasswordMail(persistedUser.getEmail());

			throw new UnprocessableEntityException()
					.addViolation("Você ainda não definiu uma senha para a sua conta. Siga as instruções no e-mail que acabamos de enviar para você.");

		} else {
			Credentials credentials = Beans.getReference(Credentials.class);
			credentials.setUsername(data.username);
			credentials.setPassword(data.password);

			securityContext.login();
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
