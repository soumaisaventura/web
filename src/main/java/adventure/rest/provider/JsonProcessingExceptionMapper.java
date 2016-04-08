package adventure.rest.provider;

import org.codehaus.jackson.JsonProcessingException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {

	@Override
	public Response toResponse(JsonProcessingException exception) {
		// TODO Mudar para Logger...
		exception.printStackTrace();
		return Response.status(SC_BAD_REQUEST).build();
	}
}
