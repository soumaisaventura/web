package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum PaymentType {

	AUTO("auto"), MANUAL("manual");

	private final String value;

	PaymentType(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static PaymentType fromValue(String value) {
		PaymentType result = null;

		for (PaymentType statusType : values()) {
			if (statusType.toString().equalsIgnoreCase(value)) {
				result = statusType;
				break;
			}
		}

		return result;
	}
}
