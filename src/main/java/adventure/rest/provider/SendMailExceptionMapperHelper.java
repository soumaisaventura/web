package adventure.rest.provider;

import static java.util.logging.Level.FINE;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.net.URI;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.gov.frameworkdemoiselle.security.Credentials;

public abstract class SendMailExceptionMapperHelper {

	@Inject
	private Logger logger;

	@Inject
	private Credentials credentials;

	@Context
	private UriInfo uriInfo;

	public Response toResponse(Exception exception) {
		int status = SC_UNAUTHORIZED;
		logger.log(FINE, "Mapeando exceção", exception);

		try {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			sendMail(credentials, baseUri);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		}

		return Response.status(status).entity(exception.getMessage() + " Siga as instruções no seu e-mail.")
				.type("text/plain").build();
	}

	protected abstract void sendMail(Credentials credentials, URI baseUri) throws Exception;
}
