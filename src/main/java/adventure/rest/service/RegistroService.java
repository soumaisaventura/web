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
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Strings;

@ValidateRequest
@Path("/api/registro")
@Produces(APPLICATION_JSON)
public class RegistroService {

	@Inject
	private UsuarioDAO dao;

	@POST
	@Transactional
	public Long create(@NotNull @Valid Usuario pessoa) {
		return dao.insert(pessoa).getId();
	}

	@GET
	@Path("/check")
	public Response check(@NotBlank @QueryParam("property") String property, @QueryParam("value") String value) {
		ValidationException validation = new ValidationException();

		if (Strings.isEmpty(value)) {
			validation.addViolation(property, "Não pode ser vazio");

		} else {
			if (property.equals("email")) {
				if (!dao.findByEmail(value).isEmpty()) {
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
			Usuario pessoa = new Usuario();
			pessoa.setNome("Urtzi Iglesias");
			pessoa.setEmail("urtzi.iglesias@vidaraid.com");
			pessoa.setSenha("abcde");

			pessoa.setNascimento(new Date());
			pessoa.setSexo(MASCULINO);

			dao.insert(pessoa);
		}
	}
}
