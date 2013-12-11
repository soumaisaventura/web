package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum Sexo {

	MASCULINO("M"), FEMININO("F");

	private final String value;

	Sexo(String value) {
		this.value = value;
	}

	@JsonValue
	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static Sexo fromValue(String value) {
		Sexo result = null;

		for (Sexo sexo : values()) {
			if (sexo.toString().equalsIgnoreCase(value)) {
				result = sexo;
				break;
			}
		}

		return result;
	}
}
