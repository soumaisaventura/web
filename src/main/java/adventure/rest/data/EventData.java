package adventure.rest.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "internal_id", "status", "name", "description", "site", "period", "location", "payment",
		"races", "organizers" })
public class EventData {

	public String id;

	@JsonProperty("internal_id")
	public Integer internalId;

	public String status;

	public String name;

	public String description;

	public String site;

	public PeriodData period;

	public LocationData location;

	public EventPaymentData payment;

	public List<RaceData> races;

	public List<UserData> organizers;

}
