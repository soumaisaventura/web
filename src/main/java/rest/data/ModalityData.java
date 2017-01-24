package rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"id", "internalId", "name"})
public class ModalityData {

    public String id;

    @JsonProperty("internal_id")
    public Integer internalId;

    public String name;
}
