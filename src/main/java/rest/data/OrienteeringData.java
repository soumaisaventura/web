package rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.validator.constraints.Length;

@JsonPropertyOrder({"nationalId", "sicardNumber"})
public class OrienteeringData {

    @Length(max = 20)
    @JsonProperty("national_id")
    public String nationalId;

    @Length(max = 20)
    @JsonProperty("sicard_number")
    public String sicardNumber;
}