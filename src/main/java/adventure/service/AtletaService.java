package adventure.service;

import static adventure.entity.Sexo.MASCULINO;
import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.Atleta;
import adventure.persistence.AtletaDAO;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("/atleta")
@ValidateRequest
@Produces(APPLICATION_JSON)
public class AtletaService {

	@Inject
	private AtletaDAO dao;

	@POST
	@Transactional
	public Long create(@NotNull @Valid Atleta atleta) {
		return dao.insert(atleta).getId();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void delete(@PathParam("id") Long id) {
		dao.delete(id);
	}

	@PUT
	@Transactional
	public void update(@NotNull @Valid Atleta atleta) {
		dao.update(atleta);
	}

	@GET
	public List<Atleta> search(@Email @QueryParam("email") String email) {
		List<Atleta> result = new ArrayList<Atleta>();

		if (Strings.isEmpty(email)) {
			result = dao.findAll();
		} else {
			result = dao.findByEmail(email);
		}

		return result;
	}

	@GET
	@Path("/check")
	public Response check(@NotBlank @QueryParam("property") String property, @NotBlank @QueryParam("value") String value) {
		Validation validation = null;
		Response response;

		if (property.equals("email") && !dao.findByEmail(value).isEmpty()) {
			validation = new Validation("email", "E-mail duplicado");
		}

		if (validation == null) {
			response = Response.ok().build();
		} else {
			response = Response.ok(validation).status(SC_PRECONDITION_FAILED).build();
		}

		return response;
	}

	@Startup
	@Transactional
	public void cargarTemporariaInicial() {
		if (dao.findAll().isEmpty()) {
			Atleta atleta = new Atleta();
			atleta.setNome("Urtzi Iglesias");
			atleta.setEmail("urtzi.iglesias@vidaraid.com");
			atleta.setSexo(MASCULINO);
			atleta.setNascimento(new Date());
			atleta.setRg("99887766-55");
			atleta.setCpf("987654321-00");
			atleta.setTelefoneCelular("61 1234-4567");
			atleta.setTelefoneResidencial("61 1234-4567");

			dao.insert(atleta);
		}
	}
}
