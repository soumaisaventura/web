package adventure.rest.data;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "city", "coords" })
public class LocationData {

	public CityData city;

	public List<HotspotData> hotspots;
}
