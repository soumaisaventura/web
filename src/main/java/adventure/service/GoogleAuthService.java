package adventure.service;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import adventure.security.OAuthSession;
import br.gov.frameworkdemoiselle.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

@Path("auth/google")
public class GoogleAuthService {

	private static HttpTransport TRANSPORT = new NetHttpTransport();

	private static JacksonFactory FACTORY = new JacksonFactory();

	@Inject
	private UserDAO dao;

	@POST
	@Transactional
	public void login(@FormParam("code") String code) throws Exception {
		User googleUser = getUserInfo(code);
		User persistedUser = dao.loadByEmail(googleUser.getEmail());

		if (persistedUser == null) {
			dao.insert(googleUser);
			login(googleUser);

		} else {
			updateInfo(googleUser, persistedUser);
			login(googleUser);
		}
	}

	private void updateInfo(User from, User to) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : Reflections.getNonStaticFields(to.getClass())) {
			if (Reflections.getFieldValue(field, to) == null) {
				Object value = Reflections.getFieldValue(field, from);
				Reflections.setFieldValue(field, to, value);
			}
		}

		dao.update(to);
	}

	private void login(User googleUser) {
		Credentials credentials = Beans.getReference(Credentials.class);
		credentials.setUsername(googleUser.getEmail());

		Beans.getReference(OAuthSession.class).activate();
		Beans.getReference(SecurityContext.class).login();
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
