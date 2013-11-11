package adventure.entity;

public enum Sexo {

	MASCULINO("M"), FEMININO("F");

	private final String value;

	Sexo(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value;
	}
}
