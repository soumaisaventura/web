package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import adventure.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

@ValidateRequest
@Path("/api/auth/google")
@Produces(APPLICATION_JSON)
public class GoogleAuthService {

	private static HttpTransport TRANSPORT = new NetHttpTransport();

	private static JacksonFactory FACTORY = new JacksonFactory();

	@Inject
	private UserDAO dao;

	@POST
	@Transactional
	public void login(@NotEmpty @FormParam("code") String code) throws Exception {
		User googleUser = getUserInfo(code);
		User persistedUser = dao.loadByEmail(googleUser.getEmail());

		if (persistedUser == null) {
			dao.insert(googleUser);

			Credentials credentials = Beans.getReference(Credentials.class);
			credentials.setEmail(googleUser.getEmail());
			credentials.setOauth(true);

			Beans.getReference(SecurityContext.class).login();

		} else {
			if (persistedUser.getFullName() == null) {
				persistedUser.setFullName(googleUser.getFullName());
			}

			if (persistedUser.getBirthday() == null) {
				persistedUser.setBirthday(googleUser.getBirthday());
			}

			if (persistedUser.getGender() == null) {
				persistedUser.setGender(googleUser.getGender());
			}

			dao.update(persistedUser);

			Credentials credentials = Beans.getReference(Credentials.class);
			credentials.setEmail(googleUser.getEmail());
			credentials.setOauth(true);

			Beans.getReference(SecurityContext.class).login();
		}
	}

	private User getUserInfo(String code) throws IOException {
		String clientId = "611475192580-k33ghah4orsl7d4r1r6qml5i4rtgnnrd.apps.googleusercontent.com";
		String clientSecret = "6n0it-JrwokA1jVvoFFSpS7I";

		GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, FACTORY, clientId,
				clientSecret, code, "postmessage").execute();
		GoogleCredential credential = new GoogleCredential.Builder().build().setFromTokenResponse(response);
		Oauth2 service = new Oauth2.Builder(TRANSPORT, FACTORY, credential).setApplicationName("Example").build();

		Userinfo userInfo = service.userinfo().get().execute();

		return new User(userInfo);
	}
}
