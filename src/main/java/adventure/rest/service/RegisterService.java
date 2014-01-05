package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.spi.validation.ValidateRequest;


@ValidateRequest
@Path("/api/register")
@Produces(APPLICATION_JSON)
public class RegisterService {

}
