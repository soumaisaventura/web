package adventure.rest.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "internal_id", "name", "description", "site", "period", "location", "races", "organizers",
		"layout", "status" })
public class EventData {

	public String id;

	@JsonProperty("internal_id")
	public Integer internalId;

	public String name;

	public String description;

	public String site;

	public PeriodData period;

	public LocationData location;

	public List<RaceData> races;

	public List<UserData> organizers;

	public LayoutData layout;

	public String status;
}
