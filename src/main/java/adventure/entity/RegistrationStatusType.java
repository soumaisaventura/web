package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum RegistrationStatusType {

	PENDENT("pendent", "Aguardando pagamento"), CONFIRMED("confirmed", "Confirmada"), CANCELLED("cancelled",
			"Cancelada");

	private final String value;

	private final String description;

	RegistrationStatusType(String value, String description) {
		this.value = value;
		this.description = description;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	public String description() {
		return this.description;
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
