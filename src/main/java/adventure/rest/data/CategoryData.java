package adventure.rest.data;

import adventure.entity.Category;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.validator.constraints.NotEmpty;

@JsonPropertyOrder({"id", "internalId", "name", "description", "teamSize", "minMaleMembers", "minFemaleMembers"})
public class CategoryData {

    @NotEmpty
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

    public CategoryData() {
    }

    public CategoryData(Category category) {
        this.id = category.getAlias();
        this.internalId = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.teamSize = category.getTeamSize();
        this.minMaleMembers = category.getMinMaleMembers();
        this.minFemaleMembers = category.getMinFemaleMembers();
    }
}
