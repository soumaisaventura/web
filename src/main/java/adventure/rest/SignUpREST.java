package adventure.rest;

import adventure.business.MailBusiness;
import adventure.entity.GenderType;
import adventure.entity.Health;
import adventure.entity.Profile;
import adventure.entity.User;
import adventure.persistence.HealthDAO;
import adventure.persistence.ProfileDAO;
import adventure.persistence.UserDAO;
import adventure.security.Passwords;
import adventure.validator.UniqueUserEmail;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

@Path("signup")
public class SignUpREST {

	@POST
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void signUp(SignUpData data, @Context UriInfo uriInfo) throws Exception {
		User user = new User();
		user.setEmail(data.email);
		user.setPassword(Passwords.hash(data.password.trim(), data.email.trim().toLowerCase()));
		user.setCreation(new Date());
		UserDAO.getInstance().insert(user);

		Profile profile = new Profile(user);
		profile.setName(data.name);
		profile.setBirthday(data.birthday);
		profile.setGender(data.gender);
		ProfileDAO.getInstance().insert(profile);
		HealthDAO.getInstance().insert(new Health(user));

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailBusiness.getInstance().sendUserActivation(user.getEmail(), baseUri);
	}

	@DELETE
	@LoggedIn
	@Transactional
	public void quit() {
		SecurityContext securityContext = Beans.getReference(SecurityContext.class);
		User user = (User) securityContext.getUser();
		UserDAO.getInstance().delete(user.getId());
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
		public GenderType gender;
	}

	public static class ActivationData {

		@Email
		@NotEmpty
		public String email;
	}
}
