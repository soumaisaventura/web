package adventure.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Atleta;
import adventure.entity.Telefone;
import br.gov.frameworkdemoiselle.lifecycle.Startup;

@Path("/atleta")
public class AtletaService {

	private static Map<Long, Atleta> database = Collections.synchronizedMap(new HashMap<Long, Atleta>());

	@POST
	public void create(Atleta atleta) {
		Random generator = new Random();
		Long id = Long.valueOf(generator.nextInt(99999999));

		atleta.setId(id);
		database.put(id, atleta);
	}

	@PUT
	@Path("/{id}")
	public void update(Atleta atleta) {
		database.put(atleta.getId(), atleta);
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") Long id) {
		database.remove(id);
	}

	@GET
	@Path("/{id}")
	@Produces(APPLICATION_JSON)
	public Atleta load(@PathParam("id") Long id) {
		return database.get(id);
	}

	@GET
	@Produces(APPLICATION_JSON)
	public List<Atleta> search() {
		return new ArrayList<Atleta>(database.values());
	}

	@Startup
	public void cargarTemporariaInicial() {
		Atleta atleta;

		atleta = new Atleta();
		atleta.setId(Long.valueOf(1));
		atleta.setNome("Urtzi Iglesias");
		atleta.setEmail("urtzi.iglesias@vidaraid.com");

		Calendar calendar = Calendar.getInstance();
		calendar.set(1980, 01, 01);
		atleta.setNascimento(calendar.getTime());

		atleta.setRg(null);
		atleta.setCpf(null);
		atleta.setCelular(new Telefone("61", "1234-4567"));
		atleta.setResidencial(null);
		atleta.setComercial(new Telefone("61", "0011-2233"));

		create(atleta);
	}
}
