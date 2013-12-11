package adventure.scrap;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import adventure.entity.Evento;

@Path("/api/scrap")
@Produces(APPLICATION_JSON)
public class ScrapService {

	@GET
	@Path("/evento")
	public List<Evento> carregarEventos() {
		return AdventureScrap.getEventFromAdventureMag();
	}
}
