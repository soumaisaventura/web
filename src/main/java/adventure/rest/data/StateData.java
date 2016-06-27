package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"id", "internalId", "name", "country"})
public class StateData {

    public String id;

    @JsonProperty("internal_id")
    public Integer internalId;

    public String name;

    public CountryData country;
}
