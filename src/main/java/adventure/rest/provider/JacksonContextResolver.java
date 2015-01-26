package adventure.rest.provider;

import static org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS;
import static org.codehaus.jackson.map.SerializationConfig.Feature.INDENT_OUTPUT;
import static org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS;
import static org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.text.SimpleDateFormat;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

@Provider
@Consumes("application/json")
@Produces("application/json")
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;

	public JacksonContextResolver() throws Exception {
		SimpleModule module = new SimpleModule("App Custom Module", new Version(1, 0, 0, null));
		module.addDeserializer(String.class, new EmptyStringAsNull());

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(module);
		objectMapper.configure(INDENT_OUTPUT, true);
		objectMapper.configure(WRITE_ENUMS_USING_TO_STRING, true);
		objectMapper.configure(WRITE_EMPTY_JSON_ARRAYS, false);
		objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(NON_NULL);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	}

	@Override
	public ObjectMapper getContext(Class<?> arg0) {
		return objectMapper;
	}
}
