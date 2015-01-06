package adventure.client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import adventure.rest.SignUpREST.SignUpData;

@Path("signup")
@Consumes(APPLICATION_JSON)
public interface SignUpClient {

	@POST
	public Long signup(SignUpData form);

	@DELETE
	public void quit();
}
