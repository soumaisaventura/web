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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Gender;
import adventure.entity.User;
import adventure.persistence.MailDAO;
import adventure.persistence.UserDAO;
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
	private UserDAO userDAO;

	@POST
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	@Produces("application/json")
	public Long signUp(SignUpData data, @Context UriInfo uriInfo) throws Exception {
		User user = new User();
		user.setName(data.name);
		user.setEmail(data.email);
		user.setPassword(data.password);
		user.setBirthday(data.birthday);
		user.setGender(data.gender);
		user.setCreation(new Date());

		String password = user.getPassword();
		user.setPassword(Passwords.hash(password));
		Long result = userDAO.insert(user).getId();

		login(user.getEmail(), password);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		Beans.getReference(MailDAO.class).sendAccountActivationMail(user.getEmail(), baseUri);

		return result;
	}

	@POST
	@Transactional
	@ValidatePayload
	@Path("/activation/{token}")
	@Consumes("application/json")
	public void activate(@PathParam("token") String token, ActivationData data) throws Exception {
		UserDAO dao = Beans.getReference(UserDAO.class);
		User persistedUser = dao.loadByEmail(data.email);
		String persistedToken = persistedUser.getActivationToken();

		if (persistedToken == null || !persistedToken.equals(token)) {
			throw new UnprocessableEntityException().addViolation("Solicitação inválida");

		} else {
			persistedUser.setActivationToken(null);
			persistedUser.setActivation(new Date());
			dao.update(persistedUser);
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
		User user = (User) securityContext.getUser();
		userDAO.delete(user.getId());
	}

	// @Startup
	// @Transactional
	// public void cargaTemporariaInicial() {
	// if (userDAO.findAll().isEmpty()) {
	// User usuario;
	//
	// usuario = new User();
	// usuario.setName("Urtzi Iglesias");
	// usuario.setEmail("urtzi.iglesias@vidaraid.com");
	// usuario.setPassword(Passwords.hash("abcde"));
	// usuario.setActivation(new Date());
	// usuario.setBirthday(new Date());
	// usuario.setGender(MALE);
	// userDAO.insert(usuario);
	// }
	// }

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
