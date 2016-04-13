package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"name", "members"})
public class TeamData {

    @NotEmpty
    public String name;

    @Valid
    @NotEmpty
    public List<UserData> members = new ArrayList();
}
