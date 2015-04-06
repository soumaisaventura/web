package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum RaceStatusType {

	SOON("soon"), OPEN("open"), CLOSED("closed"), END("end");

	private final String value;

	RaceStatusType(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static RaceStatusType fromValue(String value) {
		RaceStatusType result = null;

		for (RaceStatusType statusType : values()) {
			if (statusType.toString().equalsIgnoreCase(value)) {
				result = statusType;
				break;
			}
		}

		return result;
	}
}
