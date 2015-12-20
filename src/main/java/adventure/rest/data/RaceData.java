package adventure.rest.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "internal_id", "status", "name", "description", "distance", "period", "event", "sport",
		"championships", "categories", "currentPeriod", "prices", "modalities" })
public class RaceData {

	public String id;

	@JsonProperty("internal_id")
	public Integer internalId;

	public String status;

	public String name;

	public String description;

	public Integer distance;

	public PeriodData period;

	public EventData event;

	public SportData sport;

	public List<ChampionshipData> championships;

	public List<CategoryData> categories;

	@JsonProperty("current_period")
	public PeriodData currentPeriod;

	public List<PeriodData> prices;

	public List<ModalityData> modalities;

}
