package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import adventure.security.Credentials;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;

@ValidateRequest
@Path("/api/auth/facebook")
@Produces(APPLICATION_JSON)
public class FacebookAuthService {

	@Inject
	private UserDAO dao;

	@POST
	@Transactional
	public void login(@NotEmpty @FormParam("code") String code) throws Exception {
		User facebookUser = getUserInfo(code);
		User persistedUser = dao.loadByEmail(facebookUser.getEmail());

		if (persistedUser == null) {
			dao.insert(facebookUser);
			login(facebookUser);

		} else {
			updateInfo(facebookUser, persistedUser);
			login(facebookUser);
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

	private User getUserInfo(String accessToken) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();

		String newUrl = "https://graph.facebook.com/me?access_token=" + accessToken;

		httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(newUrl);

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpclient.execute(httpget, responseHandler);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		JsonNode rootNode = mapper.readTree(responseBody);

		User user = new User();
		user.setFullName(rootNode.get("name").asText());
		user.setEmail(rootNode.get("email").asText());

		// JsonNode location = firstResult.get("locations").get(0);
		// JsonNode latLng = location.get("latLng");
		// String lat = latLng.get("lat").asText();
		// String lng = latLng.get("lng").asText();
		// output = lat + "," + lng;
		// System.out.println("Found Coordinates: " + output);

		// JSONObject json = (JSONObject) JSONSerializer.toJSON(responseBody);
		// String facebookId = json.getString("id");
		// String firstName = json.getString("first_name");
		// String lastName = json.getString("last_name");
		// email = json.getString("email");
		// // put user data in session
		// httpSession.setAttribute("FACEBOOK_USER", firstName + " " + lastName + ", facebookId:" + facebookId);

		httpclient.getConnectionManager().shutdown();

		return user;
	}
}
