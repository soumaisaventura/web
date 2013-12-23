package adventure.rest.mapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import br.gov.frameworkdemoiselle.security.NotLoggedInException;
import br.gov.frameworkdemoiselle.util.Beans;

@Provider
public class NotLoggedInExceptionMapper implements ExceptionMapper<NotLoggedInException> {

	@Override
	public Response toResponse(NotLoggedInException exception) {
		HttpServletRequest request = Beans.getReference(HttpServletRequest.class);
		String path = request.getRequestURI().substring(request.getContextPath().length());

		Response response;

		if (path.indexOf("/api") > -1) {
			response = Response.status(401).header("WWW-Authenticate", "Basic realm=realm").build();
		} else {
			response = Response.status(401).build();
		}

		return response;
	}
}