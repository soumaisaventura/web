package adventure.rest.provider;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import br.gov.frameworkdemoiselle.util.Strings;

public class EmptyStringAsNull extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String string = jp.getText();

		return Strings.isEmpty(string) ? null : string;
	}
}
