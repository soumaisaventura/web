package adventure.rest.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import adventure.entity.StatusType;

@JsonPropertyOrder({ "id", "name", "description", "site", "period", "location", "races", "organizers", "layout",
		"status" })
public class EventData {

	public String id;

	public String name;

	public String description;

	public String site;

	public PeriodData period;

	public LocationData location;

	public List<RaceData> races;

	public List<UserData> organizers;

	public LayoutData layout;

	public StatusType status;
}
