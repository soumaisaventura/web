package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum EventPaymentType {

	AUTO("auto"), MANUAL("manual");

	private final String value;

	EventPaymentType(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static EventPaymentType fromValue(String value) {
		EventPaymentType result = null;

		for (EventPaymentType statusType : values()) {
			if (statusType.toString().equalsIgnoreCase(value)) {
				result = statusType;
				break;
			}
		}

		return result;
	}
}