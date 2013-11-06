package adventure.entity;

public class Telefone {

	private String area;

	private String numero;

	public Telefone() {
	}

	public Telefone(String area, String numero) {
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
