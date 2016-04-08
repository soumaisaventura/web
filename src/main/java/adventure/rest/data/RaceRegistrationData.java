package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static adventure.util.Constants.NAME_SIZE;

public class RaceRegistrationData {

    @NotEmpty
    @Size(max = NAME_SIZE)
    @JsonProperty("team_name")
    public String teamName;

    @NotNull
    @JsonProperty("category_id")
    public Integer categoryId;

    @NotEmpty
    @JsonProperty("members_ids")
    public List<Integer> membersIds;
}
