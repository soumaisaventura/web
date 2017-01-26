package rest.exception;

import br.gov.frameworkdemoiselle.security.Credentials;
import core.business.MailBusiness;
import temp.security.PasswordNotDefinedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;

@Provider
public class PasswordNotDefinedExceptionMapper extends SendMailExceptionMapperHelper implements
        ExceptionMapper<PasswordNotDefinedException> {

    @Override
    public Response toResponse(PasswordNotDefinedException exception) {
        return super.toResponse(exception);
    }

    @Override
    protected void sendMail(Credentials credentials, URI baseUri) throws Exception {
        MailBusiness.getInstance().sendPasswordCreationMail(credentials.getUsername(), baseUri);
    }
}
