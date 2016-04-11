package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"id", "name", "state", "country"})
public class CityData {

    @NotNull(message = "{adventure.validation.constraints.NotNullCityId.message}")
    public Integer id;

    public String name;

    public String state;

    public String country;
}
