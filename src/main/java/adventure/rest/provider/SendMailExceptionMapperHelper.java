package adventure.rest.provider;

import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.NameQualifier;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public abstract class SendMailExceptionMapperHelper {

	private Logger logger;

	@Context
	private UriInfo uriInfo;

	protected abstract void sendMail(Credentials credentials, URI baseUri) throws Exception;

	public Response toResponse(Exception exception) {
		int status = SC_UNAUTHORIZED;
		getLogger().log(FINE, "Mapeando exceção", exception);

		try {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			sendMail(Beans.getReference(Credentials.class), baseUri);
		} catch (Exception cause) {
			throw new RuntimeException(cause);
		}

		return Response.status(status).entity(exception.getMessage() + " Siga as instruções no seu e-mail.")
				.type("text/plain").build();
	}

	protected Logger getLogger() {
		if (logger == null) {
			this.logger = Beans.getReference(Logger.class, new NameQualifier(this.getClass().getName()));
		}

		return this.logger;
	}
}
