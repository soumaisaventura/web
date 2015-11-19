package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "email", "mobile" })
public class UserData {

	public Integer id;

	public String name;

	public String email;

	public String mobile;
}
