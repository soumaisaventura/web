package adventure.rest.data;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "latitude", "longitude" })
public class CoordData {

	public BigDecimal latitude;

	public BigDecimal longitude;
}
