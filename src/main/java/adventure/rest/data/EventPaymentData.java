package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import adventure.entity.EventPaymentType;

@JsonPropertyOrder({ "type", "info", "code", "transaction" })
public class EventPaymentData {

	public EventPaymentType type;

	public String info;

	public String code;

	public String transaction;
}
