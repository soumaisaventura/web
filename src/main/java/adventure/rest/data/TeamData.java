package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name", "members"})
public class TeamData {

    public String name;

    public List<UserData> members = new ArrayList<UserData>();

}
