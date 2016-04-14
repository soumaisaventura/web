package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonPropertyOrder({"category", "team"})
public class RaceRegistrationData {

    @Valid
    @NotNull
    public CategoryData category;

    @Valid
    @NotNull
    public TeamData team;
}
