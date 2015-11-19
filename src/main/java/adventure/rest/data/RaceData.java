package adventure.rest.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import adventure.entity.RaceStatusType;

public class RaceData {

	public String id;

	public String name;

	public String description;

	public SportData sport;

	public List<ChampionshipData> championships;

	public PeriodData period;

	public List<CategoryData> categories;

	@JsonProperty("current_period")
	public PeriodData currentPeriod;

	public List<PeriodData> prices;

	public List<ModalityData> modalities;

	public RaceStatusType status;
}
