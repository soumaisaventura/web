package adventure.rest.data;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "members" })
public class TeamData {

	public String name;

	public List<UserData> members = new ArrayList<UserData>();

}
