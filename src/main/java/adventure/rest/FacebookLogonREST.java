package adventure.rest;

import adventure.entity.GenderType;
import adventure.entity.Profile;
import adventure.entity.User;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Path("logon/oauth/facebook")
public class FacebookLogonREST extends OAuthLogon {

    @Override
    protected Created createUser(String code) throws Exception {
        HttpClient client = new DefaultHttpClient();

        String newUrl = "https://graph.facebook.com/me?access_token=" + code;
        HttpGet httpget = new HttpGet(newUrl);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = client.execute(httpget, responseHandler);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode rootNode = mapper.readTree(responseBody);

        if (!rootNode.get("verified").asBoolean()) {
            throw new IllegalStateException("O e-mail não foi verificado");
        }

        Profile profile = new Profile();
        profile.setName(rootNode.get("name").asText());
        // profile.setPicture(getPicture("http://graph.facebook.com/" + asText(rootNode.get("id"))
        // + "/picture?height=372&width=372&redirect=true"));

        // TODO Tratar gender null;

        if (asText(rootNode.get("gender")) != null) {
            // TODO Tratar gender de outro tipo adventure.entity.GenderType.OTHER
            profile.setGender(GenderType.valueOf(asText(rootNode.get("gender")).toUpperCase()));
        }

        if (rootNode.get("birthday") != null) {
            DateFormat format = new SimpleDateFormat("MM/dd/yyyyy");
            profile.setBirthday(format.parse(asText(rootNode.get("birthday"))));
        }

        User user = new User();
        // TODO Tratar email nulo
        user.setEmail(rootNode.get("email").asText());
        user.setProfile(profile);
        user.setFacebookId(asText(rootNode.get("id")));
        user.setFacebookToken(code);

        client.getConnectionManager().shutdown();

        return new Created(user, "http://graph.facebook.com/" + user.getFacebookId()
                + "/picture?height=372&width=372&redirect=true");
    }

    private String asText(JsonNode node) {
        return node != null ? node.asText() : null;
    }
}
