package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.spi.validation.ValidateRequest;

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

	@POST
	public void login(@NotEmpty @FormParam("code") String code) throws IOException {
		HttpTransport transport = new NetHttpTransport();
		JacksonFactory factory = new JacksonFactory();
		String clientId = "611475192580-k33ghah4orsl7d4r1r6qml5i4rtgnnrd.apps.googleusercontent.com";
		String clientSecret = "6n0it-JrwokA1jVvoFFSpS7I";

		GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(transport, factory, clientId,
				clientSecret, code, "postmessage").execute();
		GoogleCredential credential = new GoogleCredential.Builder().build().setFromTokenResponse(response);
		Oauth2 userInfoService = new Oauth2.Builder(transport, factory, credential).setApplicationName("Example")
				.build();

		Userinfo userInfo = userInfoService.userinfo().get().execute();
		System.out.println(userInfo);
	}
}
