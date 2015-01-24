package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum StatusType {

	PENDENT("Pendente"), CONFIRMED("Confirmada"), CANCELLED("Cancelada");

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

		for (StatusType sexo : values()) {
			if (sexo.toString().equalsIgnoreCase(value)) {
				result = sexo;
				break;
			}
		}

		return result;
	}
}
