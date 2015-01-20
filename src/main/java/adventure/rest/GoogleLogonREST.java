package adventure.rest;

import javax.ws.rs.Path;

import adventure.entity.Gender;
import adventure.entity.Profile;
import adventure.entity.User;
import adventure.util.ApplicationConfig;
import br.gov.frameworkdemoiselle.util.Beans;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

@Path("logon/oauth/google")
public class GoogleLogonREST extends OAuthLogon {

	private static HttpTransport TRANSPORT = new NetHttpTransport();

	private static JacksonFactory FACTORY = new JacksonFactory();

	@Override
	protected User createUser(String code) throws Exception {
		ApplicationConfig config = Beans.getReference(ApplicationConfig.class);
		String clientId = config.getOAuthGoogleId();
		String clientSecret = config.getOAuthGoogleSecret();

		GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, FACTORY, clientId,
				clientSecret, code, "postmessage").execute();
		GoogleCredential credential = new GoogleCredential.Builder().build().setFromTokenResponse(response);
		Oauth2 service = new Oauth2.Builder(TRANSPORT, FACTORY, credential).setApplicationName("Example").build();

		Userinfo userInfo = service.userinfo().get().execute();

		return createUser(userInfo);
	}

	private User createUser(Userinfo userInfo) {
		if (!userInfo.getVerifiedEmail()) {
			throw new IllegalStateException("O e-mail não foi verificado");
		}

		Profile profile = new Profile();
		profile.setName(userInfo.getName());

		if (userInfo.getGender() != null) {
			profile.setGender(Gender.valueOf(userInfo.getGender().toUpperCase()));
		}

		User user = new User();
		user.setEmail(userInfo.getEmail());
		user.setProfile(profile);

		// userInfo.get("birthday");

		return user;
	}
}
