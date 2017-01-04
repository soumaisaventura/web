package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"nationalId", "sicardNumber"})
public class OrienteeringData {

    @JsonProperty("national_id")
    public String nationalId;

    @JsonProperty("sicard_number")
    public String sicardNumber;

}