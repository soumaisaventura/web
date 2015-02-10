package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum StatusType {

	PENDENT("pendent"), CONFIRMED("confirmed"), CANCELLED("cancelled");

	private final String value;

	StatusType(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static StatusType fromValue(String value) {
		StatusType result = null;

		for (StatusType statusType : values()) {
			if (statusType.toString().equalsIgnoreCase(value)) {
				result = statusType;
				break;
			}
		}

		return result;
	}
}
