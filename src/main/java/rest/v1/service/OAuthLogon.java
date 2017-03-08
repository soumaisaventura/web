package rest.v1.service;

import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import core.business.MailBusiness;
import core.business.ProfileBusiness;
import core.entity.Health;
import core.entity.User;
import core.persistence.HealthDAO;
import core.persistence.ProfileDAO;
import core.persistence.UserDAO;
import core.security.Authenticator;
import core.util.Misc;
import org.hibernate.validator.constraints.NotEmpty;
import rest.v1.data.UserData;
import temp.security.OAuthSession;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

import static rest.v1.service.LogonREST.TOKEN_RESPONSE_HEADER;

public abstract class OAuthLogon {

    protected abstract Created createUser(String code) throws Exception;

    @POST
    @Transactional
    @ValidatePayload
    @Produces("application/json")
    @Consumes("application/json")
    public UserData login(CredentialsData data, @Context UriInfo uriInfo, @Context HttpServletResponse response) throws Exception {
        String token = null;
        UserDAO userDAO = UserDAO.getInstance();
        ProfileDAO profileDAO = ProfileDAO.getInstance();

        Created created = createUser(data.token);
        User oauth = created.user;
        User persisted = UserDAO.getInstance().loadForAuthentication(oauth.getEmail());

        if (persisted == null) {
            oauth.setActivation(new Date());
            userDAO.insert(oauth);
            oauth.getProfile().setUser(oauth);
            profileDAO.insert(oauth.getProfile());
            HealthDAO.getInstance().insert(new Health(oauth));

            URI baseUri = uriInfo.getBaseUri().resolve("..");
            token = Authenticator.getInstance().authenticate(oauth);

            MailBusiness.getInstance().sendWelcome(User.getLoggedIn().getEmail(), baseUri);

        } else if (persisted.getActivation() == null) {
            oauth.setActivation(new Date());
        }

        if (persisted != null) {
            persisted = userDAO.load(persisted.getId());
            Misc.copyFields(oauth, persisted);
            userDAO.update(persisted);

            persisted.setProfile(profileDAO.load(persisted));
            Misc.copyFields(oauth.getProfile(), persisted.getProfile());
            profileDAO.update(persisted.getProfile());
            ProfileBusiness.getInstance().updatePicture(persisted.getId(), created.pictureUrl);

            token = Authenticator.getInstance().authenticate(oauth);
        }

        response.setHeader(TOKEN_RESPONSE_HEADER, token);
        return new UserData(User.getLoggedIn(), uriInfo, false);
    }

    protected void login(String username) {
        Credentials credentials = Beans.getReference(Credentials.class);
        credentials.setUsername(username);

        Beans.getReference(OAuthSession.class).activate();
        Beans.getReference(SecurityContext.class).login();
    }

    public static class CredentialsData {

        @NotEmpty
        public String token;
    }

    protected class Created {

        final private User user;

        final private String pictureUrl;

        protected Created(User user, String pictureUrl) {
            this.user = user;
            this.pictureUrl = pictureUrl;
            System.out.println("usuario " + user.getName() + " [" + user.getEmail() + "] " + pictureUrl);
        }
    }
}
