package adventure.rest.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "internal_id", "name", "description", "distance", "sport", "championships", "period",
		"categories", "currentPeriod", "prices", "modalities", "status" })
public class RaceData {

	public String id;

	@JsonProperty("internal_id")
	public Integer internalId;

	public String name;

	public String description;

	public Integer distance;

	public SportData sport;

	public List<ChampionshipData> championships;

	public PeriodData period;

	public List<CategoryData> categories;

	@JsonProperty("current_period")
	public PeriodData currentPeriod;

	public List<PeriodData> prices;

	public List<ModalityData> modalities;

	public String status;
}
