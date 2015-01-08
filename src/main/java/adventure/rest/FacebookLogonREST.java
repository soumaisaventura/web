package adventure.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ws.rs.Path;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import adventure.entity.Account;
import adventure.entity.Gender;
import adventure.entity.Profile;

@Path("logon/facebook")
public class FacebookLogonREST extends OAuthLogon {

	@Override
	protected Profile createProfile(String code) throws Exception {
		HttpClient client = new DefaultHttpClient();

		String newUrl = "https://graph.facebook.com/me?access_token=" + code;

		client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(newUrl);

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = client.execute(httpget, responseHandler);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		JsonNode rootNode = mapper.readTree(responseBody);

		if (!rootNode.get("verified").asBoolean()) {
			throw new IllegalStateException("O e-mail não foi verificado");
		}

		Account account = new Account();
		account.setEmail(rootNode.get("email").asText());

		Profile profile = new Profile();
		profile.setAccount(account);
		profile.setName(rootNode.get("name").asText());

		if (rootNode.get("gender") != null) {
			profile.setGender(Gender.valueOf(rootNode.get("gender").asText().toUpperCase()));
		}

		if (rootNode.get("birthday") != null) {
			DateFormat format = new SimpleDateFormat("MM/dd/yyyyy");
			profile.setBirthday(format.parse(rootNode.get("birthday").asText()));
		}

		client.getConnectionManager().shutdown();

		return profile;
	}
}
