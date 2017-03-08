package rest.v1.service;

import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import core.entity.User;
import core.security.Authenticator;
import core.util.ApplicationConfig;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import rest.v1.data.UserData;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("logon")
public class LogonREST {

    public static final String TOKEN_RESPONSE_HEADER = "X-Token";

    @POST
    @ValidatePayload
    @Produces("application/json")
    @Consumes("application/json")
    public UserData login(CredentialsData data, @Context UriInfo uriInfo, @Context HttpServletResponse response) throws Exception {
        String token = Authenticator.getInstance().authenticate(data.username, data.password);
        response.setHeader(TOKEN_RESPONSE_HEADER, token);
        return new UserData(User.getLoggedIn(), uriInfo, false);
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
