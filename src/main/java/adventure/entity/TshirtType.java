package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum TshirtType {

	PP("PP"), P("P"), M("M"), G("G"), GG("GG");

	private final String value;

	TshirtType(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static TshirtType fromValue(String value) {
		TshirtType result = null;

		for (TshirtType type : values()) {
			if (type.toString().equalsIgnoreCase(value)) {
				result = type;
				break;
			}
		}

		return result;
	}
}
