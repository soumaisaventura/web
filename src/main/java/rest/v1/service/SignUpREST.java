package rest.v1.service;

import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import core.business.MailBusiness;
import core.entity.GenderType;
import core.entity.Health;
import core.entity.Profile;
import core.entity.User;
import core.persistence.HealthDAO;
import core.persistence.ProfileDAO;
import core.persistence.UserDAO;
import core.util.UniqueUserEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import temp.security.Passwords;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Date;

@Path("signup")
public class SignUpREST {

    @POST
    @Transactional
    @ValidatePayload
    @Consumes("application/json")
    public void signUp(SignUpData data, @Context UriInfo uriInfo) throws Exception {
        User user = new User();
        user.setEmail(data.email);
        user.setPassword(Passwords.hash(data.password.trim(), data.email.trim().toLowerCase()));
        user.setCreation(new Date());
        UserDAO.getInstance().insert(user);

        Profile profile = new Profile(user);
        profile.setName(data.name);
        profile.setBirthday(data.birthday);
        profile.setGender(data.gender);
        ProfileDAO.getInstance().insert(profile);
        HealthDAO.getInstance().insert(new Health(user));

        URI baseUri = uriInfo.getBaseUri().resolve("..");
        MailBusiness.getInstance().sendUserActivation(user.getEmail(), baseUri);
    }

    public static class SignUpData {

        @NotEmpty
        public String name;

        @Email
        @NotEmpty
        @UniqueUserEmail
        public String email;

        @NotEmpty
        public String password;

        @Past
        @NotNull
        public Date birthday;

        @NotNull
        public GenderType gender;
    }

    public static class ActivationData {

        @Email
        @NotEmpty
        public String email;
    }
}
