package rest.v1.service;

import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;
import rest.security.LoggedIn;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("logout")
public class LogoutREST {

    @POST
    @LoggedIn
    public void logout() {
        Beans.getReference(SecurityContext.class).logout();
    }
}
