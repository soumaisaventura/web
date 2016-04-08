package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "description", "main", "coord", "order"})
public class HotspotData {

    public Integer id;

    public String name;

    public String description;

    public Boolean main;

    public CoordData coord;

    public Integer order;
}
