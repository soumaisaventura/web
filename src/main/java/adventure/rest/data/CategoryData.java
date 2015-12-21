package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "description", "teamSize", "minMaleMembers", "minFemaleMembers" })
public class CategoryData {

	public String name;

	public String description;

	@JsonProperty("team_size")
	public Integer teamSize;

	@JsonProperty("min_male_members")
	public Integer minMaleMembers;

	@JsonProperty("min_female_members")
	public Integer minFemaleMembers;
}
