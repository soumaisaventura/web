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

import adventure.entity.Gender;
import adventure.entity.Health;
import adventure.entity.Profile;
import adventure.entity.User;
import adventure.persistence.HealthDAO;
import adventure.persistence.MailDAO;
import adventure.persistence.ProfileDAO;
import adventure.persistence.UserDAO;
import adventure.security.ActivationSession;
import adventure.security.Passwords;
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
	private UserDAO userDAO;

	@POST
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void signUp(SignUpData data, @Context UriInfo uriInfo) throws Exception {
		User user = new User();
		user.setEmail(data.email);
		user.setPassword(Passwords.hash(data.password, data.email));
		user.setCreation(new Date());
		userDAO.insert(user);

		Profile profile = new Profile(user);
		profile.setName(data.name);
		profile.setBirthday(data.birthday);
		profile.setGender(data.gender);
		Beans.getReference(ProfileDAO.class).insert(profile);
		Beans.getReference(HealthDAO.class).insert(new Health(user));

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		Beans.getReference(MailDAO.class).sendUserActivation(user.getEmail(), baseUri);
	}

	@POST
	@Transactional
	@ValidatePayload
	@Path("/activation/{token}")
	@Consumes("application/json")
	public void activate(@PathParam("token") String token, ActivationData data, @Context UriInfo uriInfo)
			throws Exception {
		User persisted = userDAO.load(data.email);
		validate(token, persisted);

		login(persisted.getEmail(), token);

		persisted.setActivationToken(null);
		persisted.setActivation(new Date());
		userDAO.update(persisted);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		Beans.getReference(MailDAO.class).sendWelcome(User.getLoggedIn(), baseUri);
	}

	private void validate(String token, User user) throws Exception {
		if (user == null || user.getActivationToken() == null
				|| !user.getActivationToken().equals(Passwords.hash(token, user.getEmail()))) {
			throw new UnprocessableEntityException().addViolation("Solicitação inválida");
		}
	}

	private void login(String email, String token) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(email);

		Beans.getReference(ActivationSession.class).setToken(token);;
		Beans.getReference(SecurityContext.class).login();
	}

	@DELETE
	@LoggedIn
	@Transactional
	public void quit() {
		SecurityContext securityContext = Beans.getReference(SecurityContext.class);
		User user = (User) securityContext.getUser();
		userDAO.delete(user.getId());
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
		public String email;
	}
}
