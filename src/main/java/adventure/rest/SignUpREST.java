package adventure.rest;

import java.net.URI;
import java.util.Date;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Account;
import adventure.entity.Gender;
import adventure.entity.Health;
import adventure.entity.Profile;
import adventure.persistence.AccountDAO;
import adventure.persistence.HealthDAO;
import adventure.persistence.MailDAO;
import adventure.persistence.ProfileDAO;
import adventure.security.Passwords;
import adventure.validator.ExistentUserEmail;
import adventure.validator.UniqueUserEmail;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("signup")
public class SignUpREST {

	@Inject
	private AccountDAO accountDAO;

	@POST
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void signUp(SignUpData data, @Context UriInfo uriInfo) throws Exception {
		Account account = new Account();
		account.setEmail(data.email);
		account.setPassword(data.password);
		account.setCreation(new Date());

		Profile profile = new Profile();
		profile.setAccount(account);
		profile.setName(data.name);
		profile.setBirthday(data.birthday);
		profile.setGender(data.gender);

		String password = account.getPassword();
		account.setPassword(Passwords.hash(password));
		accountDAO.insert(account).getId();
		Beans.getReference(ProfileDAO.class).insert(profile);
		Beans.getReference(HealthDAO.class).insert(new Health(account));

		login(account.getEmail(), password);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		Beans.getReference(MailDAO.class).sendAccountActivationMail(account.getEmail(), baseUri);
	}

	@POST
	@Transactional
	@ValidatePayload
	@Path("/activation/{token}")
	@Consumes("application/json")
	public void activate(@PathParam("token") String token, ActivationData data) throws Exception {
		Account persistedAccount = accountDAO.load(data.email);
		String persistedToken = persistedAccount.getConfirmationToken();

		if (persistedToken == null || !persistedToken.equals(token)) {
			throw new UnprocessableEntityException().addViolation("Solicitação inválida");

		} else {
			persistedAccount.setConfirmationToken(null);
			persistedAccount.setConfirmation(new Date());
			accountDAO.update(persistedAccount);
		}
	}

	private void login(String email, String password) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(email);
		credentials.setPassword(password);

		Beans.getReference(SecurityContext.class).login();
	}

	@DELETE
	@LoggedIn
	@Transactional
	public void quit() {
		SecurityContext securityContext = Beans.getReference(SecurityContext.class);
		Account account = (Account) securityContext.getUser();
		accountDAO.delete(account.getId());
	}

	public static class SignUpData {

		@NotEmpty
		public String name;

		@Email
		@NotEmpty
		@UniqueUserEmail
		public String email;

		@NotEmpty
		public String password;

		@Past
		@NotNull
		public Date birthday;

		@NotNull
		public Gender gender;
	}

	public static class ActivationData {

		@Email
		@NotEmpty
		@ExistentUserEmail
		public String email;
	}
}
