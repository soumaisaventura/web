package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

@JsonPropertyOrder({"id", "internalId", "name", "description", "price"})
public class KitData {

    @NotEmpty
    public String id;

    @JsonProperty("internal_id")
    public Integer internalId;

    public String name;

    public String description;

    public BigDecimal price;
}
