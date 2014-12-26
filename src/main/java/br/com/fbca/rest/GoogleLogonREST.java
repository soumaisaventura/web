package br.com.fbca.rest;

import java.io.IOException;

import javax.ws.rs.Path;

import br.com.fbca.entity.Gender;
import br.com.fbca.entity.User;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

@Path("logon/google")
public class GoogleLogonREST extends OAuthLogon {

	private static HttpTransport TRANSPORT = new NetHttpTransport();

	private static JacksonFactory FACTORY = new JacksonFactory();

	@Override
	protected User getUserInfo(String code) throws IOException {
		String clientId = "611475192580-k33ghah4orsl7d4r1r6qml5i4rtgnnrd.apps.googleusercontent.com";
		String clientSecret = "6n0it-JrwokA1jVvoFFSpS7I";

		GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, FACTORY, clientId,
				clientSecret, code, "postmessage").execute();
		GoogleCredential credential = new GoogleCredential.Builder().build().setFromTokenResponse(response);
		Oauth2 service = new Oauth2.Builder(TRANSPORT, FACTORY, credential).setApplicationName("Example").build();

		Userinfo userInfo = service.userinfo().get().execute();

		return createUser(userInfo);
	}

	private User createUser(Userinfo userInfo) {
		User user = new User();

		if (!userInfo.getVerifiedEmail()) {
			throw new IllegalStateException("O e-mail não foi verificado");
		}

		user.setName(userInfo.getName());
		user.setEmail(userInfo.getEmail());

		if (userInfo.getGender() != null) {
			user.setGender(Gender.valueOf(userInfo.getGender().toUpperCase()));
		}

		return user;
	}
}
