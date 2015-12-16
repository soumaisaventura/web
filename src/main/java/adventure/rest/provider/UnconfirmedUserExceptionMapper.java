package adventure.rest.provider;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import adventure.business.MailBusiness;
import adventure.security.UnconfirmedUserException;
import br.gov.frameworkdemoiselle.security.Credentials;

@Provider
public class UnconfirmedUserExceptionMapper extends SendMailExceptionMapperHelper implements
		ExceptionMapper<UnconfirmedUserException> {

	@Override
	public Response toResponse(UnconfirmedUserException exception) {
		return super.toResponse(exception);
	}

	@Override
	protected void sendMail(Credentials credentials, URI baseUri) throws Exception {
		MailBusiness.getInstance().sendUserActivation(credentials.getUsername(), baseUri);
	}
}
