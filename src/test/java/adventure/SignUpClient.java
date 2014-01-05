package adventure;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import adventure.rest.service.SignUpService.SignUpForm;

@Path("/api/register")
@Consumes(APPLICATION_JSON)
public interface SignUpClient {

	@POST
	public Long signup(SignUpForm form);

	@DELETE
	public Long desregistrar();
}
