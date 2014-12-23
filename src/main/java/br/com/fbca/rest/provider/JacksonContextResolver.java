package br.com.fbca.rest.provider;

import static org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS;
import static org.codehaus.jackson.map.SerializationConfig.Feature.INDENT_OUTPUT;
import static org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING;
import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.text.SimpleDateFormat;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;

@Provider
@Produces("application/json")
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;

	public JacksonContextResolver() throws Exception {
		objectMapper = new ObjectMapper();
		objectMapper.configure(INDENT_OUTPUT, true);
		objectMapper.configure(WRITE_ENUMS_USING_TO_STRING, true);
		objectMapper.configure(FAIL_ON_EMPTY_BEANS, false);
		objectMapper.setSerializationInclusion(NON_NULL);
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	}

	@Override
	public ObjectMapper getContext(Class<?> arg0) {
		return objectMapper;
	}
}
