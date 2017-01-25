package rest.v1.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonPropertyOrder({"latitude", "longitude"})
public class CoordData {

    public BigDecimal latitude;

    public BigDecimal longitude;
}
