package adventure.rest.data;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "email", "mobile", "racePrice", "city" })
public class UserData {

	public Integer id;

	public String name;

	public String email;

	public String mobile;

	@JsonProperty("race_price")
	public BigDecimal racePrice;

	public CityData city;
}
