package adventure.rest.data;

import adventure.entity.EventPaymentType;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({ "type", "info", "code", "transaction" })
public class EventPaymentData {

	public EventPaymentType type;

	public String info;

	public String code;

	public String transaction;
}
