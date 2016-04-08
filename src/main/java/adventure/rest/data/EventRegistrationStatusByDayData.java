package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import java.util.Date;

@JsonPropertyOrder({ "date", "status" })
public class EventRegistrationStatusByDayData {

	public Date date;

	public Status status;

	@JsonPropertyOrder({ "pendent", "confirmed", "cancelled" })
	public static class Status {

		public Integer pendent;

		public Integer confirmed;

		public Integer cancelled;
	}
}
