package br.com.fbca.rest;

import static br.com.fbca.entity.Gender.MALE;

import java.util.Date;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.fbca.entity.Gender;
import br.com.fbca.entity.User;
import br.com.fbca.persistence.UserDAO;
import br.com.fbca.security.Passwords;
import br.com.fbca.validator.UniqueUserEmail;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("signup")
public class SignUpREST {

	@Inject
	private UserDAO dao;

	@POST
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	@Produces("application/json")
	public Long signUp(SignUpData data) throws Exception {
		User user = new User();
		user.setName(data.name);
		user.setEmail(data.email);
		user.setPassword(data.password);
		user.setBirthday(data.birthday);
		user.setGender(data.gender);

		String password = user.getPassword();
		user.setPassword(Passwords.hash(password));
		Long result = dao.insert(user).getId();

		login(user.getEmail(), password);

		return result;
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
		dao.delete(user.getId());
	}

	@Startup
	@Transactional
	public void cargaTemporariaInicial() {
		if (dao.findAll().isEmpty()) {
			User usuario;

			usuario = new User();
			usuario.setName("Urtzi Iglesias");
			usuario.setEmail("urtzi.iglesias@vidaraid.com");
			usuario.setPassword(Passwords.hash("abcde"));
			usuario.setBirthday(new Date());
			usuario.setGender(MALE);
			dao.insert(usuario);
		}
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
}
