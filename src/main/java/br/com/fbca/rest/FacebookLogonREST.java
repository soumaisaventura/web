package br.com.fbca.rest;

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

import br.com.fbca.entity.User;

@Path("logon/facebook")
public class FacebookLogonREST extends OAuthLogon {

	@Override
	protected User getUserInfo(String code) throws IOException {
		HttpClient httpclient = new DefaultHttpClient();

		String newUrl = "https://graph.facebook.com/me?access_token=" + code;

		httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(newUrl);

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpclient.execute(httpget, responseHandler);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		JsonNode rootNode = mapper.readTree(responseBody);

		User user = new User();
		user.setName(rootNode.get("name").asText());
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
