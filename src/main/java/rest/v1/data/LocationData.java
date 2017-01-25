package rest.v1.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"city", "coords"})
public class LocationData {

    public CityData city;

    public List<HotspotData> hotspots;
}
