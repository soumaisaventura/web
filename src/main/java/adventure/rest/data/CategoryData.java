package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "description" })
public class CategoryData {

	public String name;

	public String description;
}
