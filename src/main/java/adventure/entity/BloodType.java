package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum BloodType {

	A_POSITIVE("A+"), A_NEGATIVE("A-"), B_POSITIVE("B+"), B_NEGATIVE("B-"), AB_POSITIVE("AB+"), AB_NEGATIVE("AB-"), O_POSITIVE(
			"O+"), O_NEGATIVE("O-");

	private final String value;

	BloodType(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value.replaceAll("_POSITIVE", "+").replaceAll("_NEGATIVE", "-");
	}

	@JsonCreator
	public static BloodType fromValue(String value) {
		BloodType result = null;

		for (BloodType sexo : values()) {
			if (sexo.toString().equalsIgnoreCase(value)) {
				result = sexo;
				break;
			}
		}

		return result;
	}
}
