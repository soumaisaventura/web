package rest.v1.service;

import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import core.security.Authenticator;
import core.util.ApplicationConfig;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("logon")
public class LogonREST {

    @POST
    @ValidatePayload
    @Produces("text/plain")
    @Consumes("application/json")
    public String login(CredentialsData data, @Context UriInfo uriInfo) throws Exception {
        return Authenticator.getInstance().authenticate(data.username, data.password);
    }

    @GET
    @Path("oauth")
    @Produces("application/json")
    public AppIdData getAppIds() {
        ApplicationConfig config = Beans.getReference(ApplicationConfig.class);

        AppIdData data = new AppIdData();
        data.facebook = config.getOAuthFacebookId();
        data.google = config.getOAuthGoogleId();

        return data;
    }

    public static class CredentialsData {

        @Email
        @NotEmpty
        public String username;

        @NotEmpty
        public String password;
    }

    public static class AppIdData {

        public String facebook;

        public String google;
    }
}
