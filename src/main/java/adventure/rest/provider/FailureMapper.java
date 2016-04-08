package adventure.rest.provider;

import org.jboss.resteasy.spi.Failure;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FailureMapper implements ExceptionMapper<Failure> {

	@Override
	public Response toResponse(Failure exception) {
		int status = exception.getResponse() != null ? exception.getResponse().getStatus() : exception.getErrorCode();
		return Response.status(status).build();
	}
}
