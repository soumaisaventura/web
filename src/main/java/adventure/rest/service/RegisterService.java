package adventure.rest.service;

import static adventure.entity.Gender.MALE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.beanutils.BeanUtils;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import adventure.security.Credentials;
import adventure.security.Hasher;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@ValidateRequest
@Path("/api/register")
@Produces(APPLICATION_JSON)
public class RegisterService {

	@Inject
	private UserDAO dao;

	@POST
	// TODO @NotLoggedIn
	@Transactional
	public Long register(@NotNull @Valid Registration registration) throws Exception {
		User user = new User();
		BeanUtils.copyProperties(user, registration);

		String password = user.getPassword();
		user.setPassword(Hasher.digest(password));
		Long result = dao.insert(user).getId();

		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setEmail(user.getEmail());
		credentials.setPassword(password);

		Beans.getReference(SecurityContext.class).login();

		return result;
	}

	@DELETE
	@LoggedIn
	@Transactional
	public void unregister() {
		SecurityContext securityContext = Beans.getReference(SecurityContext.class);
		br.gov.frameworkdemoiselle.security.User user = securityContext.getUser();

		dao.delete((Long) user.getAttribute("id"));
	}

	@Startup
	@Transactional
	public void cargarTemporariaInicial() {
		if (dao.findAll().isEmpty()) {
			User usuario;

			usuario = new User();
			usuario.setFullName("Urtzi Iglesias");
			usuario.setEmail("urtzi.iglesias@vidaraid.com");
			usuario.setPassword("abcde");
			usuario.setBirthday(new Date());
			usuario.setGender(MALE);
			dao.insert(usuario);
		}
	}
}
