package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.User;
import adventure.persistence.UsuarioDAO;
import adventure.persistence.ValidationException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("/api/auth")
@Produces(APPLICATION_JSON)
public class AuthService {

	@Inject
	private SecurityContext securityContext;

	@POST
	public void login(@NotEmpty @Email @FormParam("username") String username,
			@NotEmpty @FormParam("password") String password) throws Exception {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(username);
		credentials.setPassword(password);

		securityContext.login();
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
	public Response check(@NotEmpty @Email @QueryParam("email") String email) {
		ValidationException validation = new ValidationException();

		if (!Strings.isEmpty(email)) {
			// validation.addViolation("email", "campo obrigatório");
			// } else {
			UsuarioDAO dao = Beans.getReference(UsuarioDAO.class);

			if (dao.loadByEmail(email) == null) {
				validation.addViolation(null, "e-mail inexistente");
			}
		}

		if (!validation.getConstraintViolations().isEmpty()) {
			throw validation;
		}

		return Response.ok().build();
	}
}
