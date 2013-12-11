package adventure;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import adventure.entity.Usuario;

@Path("/api")
@Consumes(APPLICATION_JSON)
public interface RegistroServiceClient {

	@POST
	@Path("/registro")
	Long registrar(Usuario atleta);

	@POST
	@Path("/desregistro")
	Long desregistrar();

	// @DELETE
	// @Path("/{id}")
	// void excluir(@PathParam("id") Long id);

	// @GET
	// List<Evento> obterTodos();

	// @GET
	// @Path("/{id}")
	// Evento obter(@PathParam("id") Long id);
}
