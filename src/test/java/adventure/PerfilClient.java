package adventure;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import adventure.entity.Usuario;

@Path("/api/perfil")
@Consumes(APPLICATION_JSON)
public interface PerfilClient {

	@GET
	@Path("/{id}")
	Usuario obter(@PathParam("id") Long id);
}
