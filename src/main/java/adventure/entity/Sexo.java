package adventure.entity;

import org.codehaus.jackson.annotate.JsonCreator;

import br.gov.frameworkdemoiselle.util.Strings;

public enum Sexo {

	MASCULINO("M"), FEMININO("F");

	private final String value;

	Sexo(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value;
	}

	@JsonCreator
	public static Sexo fromValue(String value) {
		Sexo sexo;

		if (Strings.isEmpty(value)) {
			sexo = null;
		} else {
			sexo = Sexo.valueOf(value.toUpperCase());
		}

		return sexo;
	}
}
