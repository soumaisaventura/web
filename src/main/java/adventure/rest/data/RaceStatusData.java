package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum RaceStatusData {

	SOON("soon"), OPEN("open"), CLOSED("closed"), END("end");

	private final String value;

	RaceStatusData(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static RaceStatusData fromValue(String value) {
		RaceStatusData result = null;

		for (RaceStatusData statusType : values()) {
			if (statusType.toString().equalsIgnoreCase(value)) {
				result = statusType;
				break;
			}
		}

		return result;
	}
}
