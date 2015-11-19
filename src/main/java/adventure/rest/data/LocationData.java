package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "city", "coords" })
public class LocationData {

	public CityData city;

	public CoordsData coords;
}
