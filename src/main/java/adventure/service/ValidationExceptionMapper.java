package adventure.service;

import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;

@Provider
@SuppressWarnings("deprecation")
public class ValidationExceptionMapper implements ExceptionMapper<MethodConstraintViolationException> {

	@Override
	public Response toResponse(MethodConstraintViolationException exception) {
		List<Validation> validations = new ArrayList<Validation>();

		for (MethodConstraintViolation<?> violation : exception.getConstraintViolations()) {
			validations.add(new Validation(getProperty(violation), violation.getMessage()));
		}

		return Response.ok(validations).status(SC_PRECONDITION_FAILED).build();
	}

	private static String getProperty(MethodConstraintViolation<?> violation) {
		String parts[] = violation.getPropertyPath().toString().split("\\.|\\[|\\]\\.");
		String property = null;

		if (parts.length > 1) {
			property = parts[1];

			for (String part : Arrays.copyOfRange(parts, 2, parts.length)) {
				property += "." + part;
			}
		}

		return property;
	}
}
