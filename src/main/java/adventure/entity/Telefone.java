package adventure.entity;

import org.hibernate.validator.constraints.NotBlank;

public class Telefone {

	@NotBlank
	private String area;

	@NotBlank
	private String numero;

	public Telefone() {
	}

	public Telefone(String area, String numero, TipoTelefone tipo) {
		this.area = area;
		this.numero = numero;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}
