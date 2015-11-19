package adventure.rest.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "description", "site", "period", "location", "races", "organizers", "layout" })
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
}
