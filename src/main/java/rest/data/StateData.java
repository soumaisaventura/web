package rest.data;

import core.entity.State;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "internalId", "name", "country"})
public class StateData {

    public String id;

    @JsonProperty("internal_id")
    public Integer internalId;

    public String name;

    public CountryData country;

    public StateData() {
    }

    public StateData(State state) {
        this.id = state.getAbbreviation();
        this.internalId = state.getId();
        this.name = state.getName();
        this.country = new CountryData();
        this.country.id = state.getCountry().getAbbreviation();
        this.country.internalId = state.getCountry().getId();
        this.country.name = state.getCountry().getName();
    }
}
