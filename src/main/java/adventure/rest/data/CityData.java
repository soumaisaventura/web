package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "state" })
public class CityData {

	public Integer id;

	public String name;

	public String state;
}
