package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum RegistrationStatusType {

	PENDENT("pendent"), CONFIRMED("confirmed"), CANCELLED("cancelled");

	private final String value;

	RegistrationStatusType(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static RegistrationStatusType fromValue(String value) {
		RegistrationStatusType result = null;

		for (RegistrationStatusType statusType : values()) {
			if (statusType.toString().equalsIgnoreCase(value)) {
				result = statusType;
				break;
			}
		}

		return result;
	}
}
