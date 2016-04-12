package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "internalId", "name", "description", "teamSize", "minMaleMembers", "minFemaleMembers"})
public class CategoryData {

    public String id;

    @JsonProperty("internal_id")
    public Integer internalId;

    public String name;

    public String description;

    @JsonProperty("team_size")
    public Integer teamSize;

    @JsonProperty("min_male_members")
    public Integer minMaleMembers;

    @JsonProperty("min_female_members")
    public Integer minFemaleMembers;
}
