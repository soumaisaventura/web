package adventure.rest;

import adventure.business.MailBusiness;
import adventure.business.ProfileBusiness;
import adventure.entity.Health;
import adventure.entity.User;
import adventure.persistence.HealthDAO;
import adventure.persistence.ProfileDAO;
import adventure.persistence.UserDAO;
import adventure.rest.data.UserData;
import adventure.security.OAuthSession;
import adventure.util.Misc;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import org.hibernate.validator.constraints.NotEmpty;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

public abstract class OAuthLogon {

    protected abstract Created createUser(String code) throws Exception;

    @POST
    @Transactional
    @ValidatePayload
    @Consumes("application/json")
    @Produces("application/json")
    public UserData login(CredentialsData data, @Context UriInfo uriInfo) throws Exception {
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
            login(oauth.getEmail());
            MailBusiness.getInstance().sendWelcome(User.getLoggedIn(), baseUri);

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

            login(oauth.getEmail());
        }

        return new UserData(User.getLoggedIn(), uriInfo);
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
        }
    }
}
