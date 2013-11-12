package adventure.service;

import static adventure.entity.Sexo.MASCULINO;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.Atleta;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Strings;

@ValidateRequest
@Path("/atleta")
@Produces(APPLICATION_JSON)
public class AtletaService {

	@Inject
	private EntityManager em;

	@POST
	@Transactional
	@ValidateRequest
	public Long create(@NotNull @Valid Atleta atleta) {
		em.persist(atleta);
		return atleta.getId();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void delete(@PathParam("id") Long id) {
		Atleta atleta = em.find(Atleta.class, id);
		em.remove(atleta);
	}

	@GET
	public List<Atleta> search(@QueryParam("email") String email) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("from Atleta a ");

		if (!Strings.isEmpty(email)) {
			jpql.append(" where a.email = :email ");
		}

		TypedQuery<Atleta> query = em.createQuery(jpql.toString(), Atleta.class);

		if (!Strings.isEmpty(email)) {
			query.setParameter("email", email);
		}

		return query.getResultList();
	}

	@Startup
	@Transactional
	public void cargarTemporariaInicial() {
		Atleta atleta;

		atleta = new Atleta();
		atleta.setNome("Urtzi Iglesias");
		atleta.setEmail("urtzi.iglesias@vidaraid.com");
		atleta.setSexo(MASCULINO);
		atleta.setNascimento(new Date());
		atleta.setRg("99887766-55");
		atleta.setCpf("987654321-00");
		atleta.setTelefoneCelular("61 1234-4567");
		atleta.setTelefoneResidencial("61 1234-4567");

		create(atleta);
	}
}
