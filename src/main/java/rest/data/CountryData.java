package rest.data;

import core.entity.Country;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "internalId", "name"})
public class CountryData {

    public String id;

    @JsonProperty("internal_id")
    public Integer internalId;

    public String name;

    public CountryData() {
    }

    public CountryData(Country country) {
        this.id = country.getAbbreviation();
        this.internalId = country.getId();
        this.name = country.getName();
    }
}
