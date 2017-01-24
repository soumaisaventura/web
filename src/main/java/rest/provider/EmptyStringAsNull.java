package rest.provider;

import br.gov.frameworkdemoiselle.util.Strings;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

public class EmptyStringAsNull extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String string = jp.getText();

        return Strings.isEmpty(string) ? null : string;
    }
}
