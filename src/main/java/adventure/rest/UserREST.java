package adventure.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import adventure.entity.User;
import adventure.persistence.MailDAO;
import adventure.persistence.UserDAO;
import adventure.rest.SignUpREST.ActivationData;
import adventure.security.ActivationSession;
import adventure.security.Passwords;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("user")
public class UserREST {

	@GET
	@Path("search")
	@Produces("application/json")
	public List<User> search(@QueryParam("q") String q, @QueryParam("excludes[]") List<Integer> excludes)
			throws Exception {
		validate(q);
		return UserDAO.getInstance().search(q, excludes);
	}

	@GET
	@Path("all")
	@Produces("application/json")
	public List<User> findAll() throws Exception {
		return UserDAO.getInstance().search("%", new ArrayList<Integer>());
	}

	private void validate(String q) throws Exception {
		if (Strings.isEmpty(q)) {
			throw new UnprocessableEntityException().addViolation("q", "parâmetro obrigatório");
		} else if (q.length() < 3) {
			throw new UnprocessableEntityException().addViolation("q", "deve possuir 3 ou mais caracteres");
		}
	}

	@POST
	@Transactional
	@ValidatePayload
	@Path("activation/{token}")
	@Consumes("application/json")
	public void activate(@PathParam("token") String token, ActivationData data, @Context UriInfo uriInfo)
			throws Exception {
		UserDAO userDAO = UserDAO.getInstance();
		User persisted = userDAO.load(data.email);
		validate(token, persisted);

		login(persisted.getEmail(), token);

		persisted.setActivationToken(null);
		persisted.setActivation(new Date());
		userDAO.update(persisted);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailDAO.getInstance().sendWelcome(User.getLoggedIn(), baseUri);
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
}
