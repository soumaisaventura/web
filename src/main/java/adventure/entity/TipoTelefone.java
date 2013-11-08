package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;

public enum TipoTelefone {

	CELULAR("celular"), RESIDENCIAL("residencial"), COMERCIAL("comercial");

	private final String value;

	TipoTelefone(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value.toLowerCase();
	}

	@JsonCreator
	public static TipoTelefone fromValue(String value) {
		return TipoTelefone.valueOf(value.toUpperCase());
	}
}
