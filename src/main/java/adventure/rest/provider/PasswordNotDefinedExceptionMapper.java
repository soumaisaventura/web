package adventure.rest.provider;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import adventure.persistence.MailDAO;
import adventure.security.PasswordNotDefinedException;
import br.gov.frameworkdemoiselle.security.Credentials;

@Provider
public class PasswordNotDefinedExceptionMapper extends SendMailExceptionMapperHelper implements
		ExceptionMapper<PasswordNotDefinedException> {

	@Override
	public Response toResponse(PasswordNotDefinedException exception) {
		return super.toResponse(exception);
	}

	@Override
	protected void sendMail(Credentials credentials, URI baseUri) throws Exception {
		MailDAO.getInstance().sendPasswordCreationMail(credentials.getUsername(), baseUri);
	}
}
