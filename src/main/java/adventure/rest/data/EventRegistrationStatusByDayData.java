package adventure.rest.data;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "date", "count" })
public class EventRegistrationStatusByDayData {

	public Date date;

	public Count count;

	@JsonPropertyOrder({ "pendent", "confirmed", "cancelled" })
	public static class Count {

		public Integer pendent;

		public Integer confirmed;

		public Integer cancelled;
	}
}
