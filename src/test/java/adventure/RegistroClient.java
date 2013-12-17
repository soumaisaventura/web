package adventure;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import adventure.entity.User;

@Path("/api")
@Consumes(APPLICATION_JSON)
public interface RegistroClient {

	@POST
	@Path("/registro")
	Long registrar(User atleta);

	@POST
	@Path("/desregistro")
	Long desregistrar();
}
