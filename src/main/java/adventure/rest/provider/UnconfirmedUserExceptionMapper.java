package adventure.rest.provider;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import adventure.persistence.MailDAO;
import adventure.security.UnconfirmedUserException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.util.Beans;

@Provider
public class UnconfirmedUserExceptionMapper extends SendMailExceptionMapperHelper implements
		ExceptionMapper<UnconfirmedUserException> {

	@Override
	public Response toResponse(UnconfirmedUserException exception) {
		return super.toResponse(exception);
	}

	@Override
	protected void sendMail(Credentials credentials, URI baseUri) throws Exception {
		Beans.getReference(MailDAO.class).sendUserActivation(credentials.getUsername(), baseUri);
	}

}
