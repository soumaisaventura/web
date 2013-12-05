package adventure.rest.service;

import static adventure.entity.Sexo.MASCULINO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import adventure.entity.Atleta;
import adventure.persistence.AtletaDAO;
import adventure.persistence.ValidationException;
import br.gov.frameworkdemoiselle.util.Strings;

//@Path("/registro/atleta")
//@ValidateRequest
//@Produces(APPLICATION_JSON)
public class AtletaServiceBackup {

	// @Inject
	private AtletaDAO dao;

	// @POST
	// @Transactional
	public Long create(@NotNull @Valid Atleta atleta) {
		return dao.insert(atleta).getId();
	}

	// @DELETE
	// @Path("/{id}")
	// @Transactional
	public void delete(@PathParam("id") Long id) {
		dao.delete(id);
	}

	// @PUT
	// @Transactional
	public void update(@NotNull @Valid Atleta atleta) {
		dao.update(atleta);
	}

	// @GET
	public List<Atleta> search(@Email @QueryParam("email") String email) {
		List<Atleta> result = new ArrayList<Atleta>();

		if (Strings.isEmpty(email)) {
			result = dao.findAll();
		} else {
			result = dao.findByEmail(email);
		}

		return result;
	}

	// @GET
	// @Path("/check")
	public Response check(@NotBlank @QueryParam("property") String property, @QueryParam("value") String value,
			@QueryParam("id") Long id) {

		ValidationException exception = new ValidationException();
		List<Atleta> atletas = null;

		if (Strings.isEmpty(value)) {
			exception.addViolation(property, "Não pode ser vazio");
		} else {
			if (property.equals("email")) {
				atletas = dao.findByEmail(value);
				if (id != null) {
					atletas.remove(new Atleta(id));
				}
				if (!atletas.isEmpty()) {
					exception.addViolation("email", "E-mail duplicado");
				}
			} else if (property.equals("cpf")) {
				atletas = dao.findByCpf(value);
				if (id != null) {
					atletas.remove(new Atleta(id));
				}
				if (!atletas.isEmpty()) {
					exception.addViolation("cpf", "CPF duplicado");
				}
			} else if (property.equals("rg")) {
				atletas = dao.findByRg(value);
				if (id != null) {
					atletas.remove(new Atleta(id));
				}
				if (!atletas.isEmpty()) {
					exception.addViolation("rg", "RG duplicado");
				}
			}
		}

		if (!exception.getConstraintViolations().isEmpty()) {
			throw exception;
		}

		return Response.ok().build();
	}

	// @Startup
	// @Transactional
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
