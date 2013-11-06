package adventure;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import adventure.entity.Atleta;

@Path("/api/atleta")
public interface AtletaServiceClient {

	@GET
	@Consumes(APPLICATION_JSON)
	List<Atleta> search();
}
