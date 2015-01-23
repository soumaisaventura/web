package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum GenderType {

	MALE("M"), FEMALE("F");

	private final String value;

	GenderType(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static GenderType fromValue(String value) {
		GenderType result = null;

		for (GenderType sexo : values()) {
			if (sexo.toString().equalsIgnoreCase(value)) {
				result = sexo;
				break;
			}
		}

		return result;
	}
}
