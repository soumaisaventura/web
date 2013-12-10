package adventure.scrap;

public class Evento {

	private String nome;
	private String data;
	private String local;
	private String link;

	public Evento(String nome, String data, String local, String link) {
		this.nome = nome;
		this.data = data;
		this.local = local;
		this.link = link;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
