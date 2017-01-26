package rest.exception;

import br.gov.frameworkdemoiselle.security.Credentials;
import core.business.MailBusiness;
import temp.security.UnconfirmedUserException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;

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
