package adventure.service;

import static adventure.entity.Sexo.MASCULINO;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.Atleta;
import adventure.entity.Telefone;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("/atleta")
@Produces(APPLICATION_JSON)
@ValidateRequest
public class AtletaService {

	@Inject
	private EntityManager em;

	@POST
	@Transactional
	@ValidateRequest
	public Long create(@Valid Atleta atleta) {
		em.persist(atleta);
		return atleta.getId();
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void delete(@PathParam("id") Long id) {
		em.remove(em.find(Atleta.class, id));
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

		Calendar calendar = Calendar.getInstance();
		calendar.set(1980, 01, 01);
		atleta.setNascimento(calendar.getTime());

		atleta.setRg(null);
		atleta.setCpf(null);
		atleta.setTelefoneCelular(new Telefone("61", "1234-4567"));
		atleta.setTelefoneResidencial(new Telefone("61", "1234-4567"));

		create(atleta);
	}
}
