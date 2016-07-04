package adventure.rest;

import adventure.business.MailBusiness;
import adventure.entity.User;
import adventure.persistence.UserDAO;
import adventure.rest.SignUpREST.ActivationData;
import adventure.rest.data.UserData;
import adventure.security.ActivationSession;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("user")
public class UserREST {

    @GET
    @Path("search")
    @Produces("application/json")
    public List<UserData> search(@QueryParam("q") String q, @QueryParam("excludes[]") List<Integer> excludes,
                                 @Context UriInfo uriInfo) throws Exception {
        validate(q);
        List<UserData> result = new ArrayList();

        for (User user : UserDAO.getInstance().search(q, excludes)) {
            result.add(new UserData(user, uriInfo, true));
        }

        return result.isEmpty() ? null : result;
    }

    private void validate(String q) throws Exception {
        if (Strings.isEmpty(q)) {
            throw new UnprocessableEntityException().addViolation("q", "parâmetro obrigatório");
        } else if (q.length() < 3) {
            throw new UnprocessableEntityException().addViolation("q", "deve possuir 3 ou mais caracteres");
        }
    }

    @POST
    @Transactional
    @ValidatePayload
    @Path("activation/{token}")
    @Consumes("application/json")
    @Produces("application/json")
    public UserData activate(@PathParam("token") String token, ActivationData data, @Context UriInfo uriInfo)
            throws Exception {
        UserDAO userDAO = UserDAO.getInstance();
        User persisted = userDAO.load(data.email);
        validate(token, persisted);

        login(persisted.getEmail(), token);

        persisted.setActivationToken(null);
        persisted.setActivation(new Date());
        userDAO.update(persisted);

        URI baseUri = uriInfo.getBaseUri().resolve("..");
        MailBusiness.getInstance().sendWelcome(User.getLoggedIn(), baseUri);

        return new UserData(User.getLoggedIn(), uriInfo, false);
    }

    private void validate(String token, User user) throws Exception {
        if (user == null || user.getActivationToken() == null || !user.getActivationToken().equals(token)) {
            throw new UnprocessableEntityException().addViolation("Solicitação inválida");
        }
    }

    private void login(String email, String token) {
        Credentials credentials = Beans.getReference(Credentials.class);
        credentials.setUsername(email);

        Beans.getReference(ActivationSession.class).setToken(token);
        Beans.getReference(SecurityContext.class).login();
    }
}
