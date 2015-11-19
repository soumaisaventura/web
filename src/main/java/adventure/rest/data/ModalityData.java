package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name" })
public class ModalityData {

	public String id;

	public String name;

}
