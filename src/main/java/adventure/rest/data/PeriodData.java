package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Date;

@JsonPropertyOrder({ "internal_id", "countdown", "beginning", "end", "price" })
public class PeriodData {

	@JsonProperty("internal_id")
	public Integer internalId;

	public Integer countdown;

	public Date beginning;

	public Date end;

	public BigDecimal price;
}
