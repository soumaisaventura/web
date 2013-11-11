package adventure.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.text.SimpleDateFormat;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

@Provider
@Produces(APPLICATION_JSON)
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

	private static ObjectMapper objectMapper;
	{
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		objectMapper.setSerializationInclusion(NON_NULL);

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d yyyy");
		objectMapper.setDateFormat(dateFormat);
	}

	@Override
	public ObjectMapper getContext(Class<?> objectType) {
		return objectMapper;
	}
}
