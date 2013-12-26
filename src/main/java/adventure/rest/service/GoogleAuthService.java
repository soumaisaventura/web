package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.lang.reflect.Field;

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
import br.gov.frameworkdemoiselle.util.Reflections;

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
		credentials.setEmail(googleUser.getEmail());
		credentials.setOauth(true);

		Beans.getReference(SecurityContext.class).login();
	}

	private User getUserInfo(String code) throws IOException {
		String clientId = "611475192580-k33ghah4orsl7d4r1r6qml5i4rtgnnrd.apps.googleusercontent.com";
		String clientSecret = "6n0it-JrwokA1jVvoFFSpS7I";
		// String redirectURI = "http://localhost:8080";
		// String grantType = "authorization_code";

		// GoogleClient googleClient = ProxyFactory.create(GoogleClient.class, "https://accounts.google.com");
		// Token token = googleClient.exchangeForToken(code.split("\\.")[0], clientId, clientSecret, redirectURI,
		// grantType);

		GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, FACTORY, clientId,
				clientSecret, code, "postmessage").execute();
		GoogleCredential credential = new GoogleCredential.Builder().build().setFromTokenResponse(response);
		Oauth2 service = new Oauth2.Builder(TRANSPORT, FACTORY, credential).setApplicationName("Example").build();

		Userinfo userInfo = service.userinfo().get().execute();

		return new User(userInfo);
	}

	// @Path("/o/oauth2")
	// @Consumes(APPLICATION_JSON)
	// interface GoogleClient {
	//
	// @POST
	// @Path("/token")
	// Token exchangeForToken(@FormParam("code") String code, @FormParam("client_id") String clientId,
	// @FormParam("client_secret") String clientSecret, @FormParam("redirect_uri") String redirectURI,
	// @FormParam("grant_type") String grantType);
	// }
	//
	// class Token {
	//
	// private String access_token;
	//
	// private long expires_in;
	//
	// private String token_type;
	//
	// public String getAccess_token() {
	// return access_token;
	// }
	//
	// public void setAccess_token(String access_token) {
	// this.access_token = access_token;
	// }
	//
	// public long getExpires_in() {
	// return expires_in;
	// }
	//
	// public void setExpires_in(long expires_in) {
	// this.expires_in = expires_in;
	// }
	//
	// public String getToken_type() {
	// return token_type;
	// }
	//
	// public void setToken_type(String token_type) {
	// this.token_type = token_type;
	// }
	// }
}
