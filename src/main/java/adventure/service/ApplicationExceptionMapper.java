package adventure.service;

import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import adventure.persistence.ApplicationException;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

	@Override
	public Response toResponse(ApplicationException exception) {
		List<Validation> validations = new ArrayList<Validation>();

		return Response.ok(validations).status(SC_PRECONDITION_FAILED).build();
	}
}
