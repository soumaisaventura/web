package adventure.rest.data;

import adventure.entity.City;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"id", "name", "state"})
public class CityData {

    @NotNull(message = "{adventure.validation.constraints.NotNullCityId.message}")
    public Integer id;

    public String name;

    public StateData state;

    public CityData() {
    }

    public CityData(City city) {
        this.id = city.getId();
        this.name = city.getName();
        this.state = new StateData();
        this.state.id = city.getState().getAbbreviation();
        this.state.internalId = city.getState().getId();
        this.state.name = city.getState().getName();
        this.state.country = new CountryData();
        this.state.country.id = city.getState().getCountry().getAbbreviation();
        this.state.country.internalId = city.getState().getCountry().getId();
        this.state.country.name = city.getState().getCountry().getName();
    }
}
