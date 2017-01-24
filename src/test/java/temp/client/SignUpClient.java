package temp.client;

import rest.SignUpREST.SignUpData;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("signup")
@Consumes(APPLICATION_JSON)
public interface SignUpClient {

    @POST
    Long signup(SignUpData form);

    @DELETE
    void quit();
}
