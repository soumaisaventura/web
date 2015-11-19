package adventure.rest.data;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "beginning", "end", "price" })
public class PeriodData {

	public Date beginning;

	public Date end;

	public BigDecimal price;
}
