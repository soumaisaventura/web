package adventure.rest;

import java.io.IOException;

import javax.ws.rs.Path;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import adventure.entity.Profile;
import adventure.entity.Account;

@Path("logon/facebook")
public class FacebookLogonREST extends OAuthLogon {

	@Override
	protected Profile createProfile(String code) throws IOException {
		HttpClient client = new DefaultHttpClient();

		String newUrl = "https://graph.facebook.com/me?access_token=" + code;

		client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(newUrl);

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = client.execute(httpget, responseHandler);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		JsonNode rootNode = mapper.readTree(responseBody);

		Account account = new Account();
		account.setEmail(rootNode.get("email").asText());

		Profile profile = new Profile();
		profile.setAccount(account);
		profile.setName(rootNode.get("name").asText());

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

		client.getConnectionManager().shutdown();

		return profile;
	}
}
