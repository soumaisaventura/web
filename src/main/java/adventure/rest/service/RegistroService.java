package adventure.rest.service;

import static adventure.entity.Sexo.MASCULINO;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Date;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotBlank;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.Usuario;
import adventure.persistence.UsuarioDAO;
import adventure.persistence.ValidationException;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.security.User;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

@ValidateRequest
@Path("/api")
@Produces(APPLICATION_JSON)
public class RegistroService {

	@Inject
	private UsuarioDAO dao;

	@POST
	@Path("/registro")
	@Transactional
	public Long registrar(@NotNull @Valid Usuario pessoa) {
		Long result = dao.insert(pessoa).getId();

		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(pessoa.getEmail());
		credentials.setPassword(pessoa.getSenha());

		SecurityContext securityContext = Beans.getReference(SecurityContext.class);
		securityContext.login();

		return result;
	}

	@POST
	@LoggedIn
	@Transactional
	@Path("/desregistro")
	public void desregistrar() {
		SecurityContext securityContext = Beans.getReference(SecurityContext.class);
		User user = securityContext.getUser();

		dao.delete((Long) user.getAttribute("id"));
	}

	@GET
	@Path("/check")
	public Response checar(@NotBlank @QueryParam("property") String property, @QueryParam("value") String value) {
		ValidationException validation = new ValidationException();

		if (Strings.isEmpty(value)) {
			validation.addViolation(property, "Não pode ser vazio");

		} else {
			if (property.equals("email")) {
				if (dao.loadByEmail(value) != null) {
					validation.addViolation("email", "E-mail duplicado");
				}
			}
		}

		if (!validation.getConstraintViolations().isEmpty()) {
			throw validation;
		}

		return Response.ok().build();
	}

	@Startup
	@Transactional
	public void cargarTemporariaInicial() {
		if (dao.findAll().isEmpty()) {
			Usuario usuario;

			usuario = new Usuario();
			usuario.setNome("Urtzi Iglesias");
			usuario.setEmail("urtzi.iglesias@vidaraid.com");
			usuario.setSenha("abcde");
			usuario.setNascimento(new Date());
			usuario.setSexo(MASCULINO);
			dao.insert(usuario);
		}
	}
}
