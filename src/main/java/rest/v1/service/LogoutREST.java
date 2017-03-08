package rest.v1.service;

import br.gov.frameworkdemoiselle.security.LoggedIn;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("logout")
public class LogoutREST {

    @POST
    @LoggedIn
    public void logout() {
    }
}
