package adventure.rest.service;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.User;
import adventure.persistence.MailDAO;
import adventure.persistence.UserDAO;
import adventure.persistence.ValidationException;
import adventure.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

@ValidateRequest
@Path("/api/auth")
@Produces(APPLICATION_JSON)
public class AuthService {

	@Inject
	private SecurityContext securityContext;

	@POST
	public Response login(@NotNull @Valid Credentials credentials) throws Exception {
		Response response = null;

		UserDAO dao = Beans.getReference(UserDAO.class);
		User persistedUser = dao.loadByEmail(credentials.getEmail());

		if (persistedUser != null && persistedUser.getPassword() == null) {
			// TODO Mandar email

			MailDAO mailDAO = Beans.getReference(MailDAO.class);
			mailDAO.sendMail(persistedUser.getEmail());

			String message = "Mensagem teste que vai ser bem extensa. Vai falar sobre mensagem enviada para o e-mail.";
			response = Response.status(SC_FORBIDDEN).entity(message).build();

		} else {
			BeanUtils.copyProperties(Beans.getReference(Credentials.class), credentials);
			securityContext.login();

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
	public Response check(@NotEmpty @Email @QueryParam("email") String email) {
		ValidationException validation = new ValidationException();

		if (!Strings.isEmpty(email)) {
			// validation.addViolation("email", "campo obrigatório");
			// } else {
			UserDAO dao = Beans.getReference(UserDAO.class);

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
