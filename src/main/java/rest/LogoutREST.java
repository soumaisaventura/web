package rest;

import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;

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
