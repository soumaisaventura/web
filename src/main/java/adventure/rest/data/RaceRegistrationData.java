package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"category", "team"})
public class RaceRegistrationData {

//    @NotEmpty
//    @Size(max = NAME_SIZE)
//    @JsonProperty("team_name")
//    public String teamName;
//
//    @NotNull
//    @JsonProperty("category_id")
//    public Integer categoryId;
//
//    @NotEmpty
//    @JsonProperty("members_ids")
//    public List<Integer> membersIds;

    @Valid
    @NotNull
    public CategoryData category;

    @Valid
    @NotNull
    public TeamData team;
}
