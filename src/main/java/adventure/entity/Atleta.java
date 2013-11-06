package adventure.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Atleta {

	private Long id;

	private String nome;

	private String email;

	private Date nascimento;

	private String rg;

	private String cpf;

	private List<Telefone> telefones = Collections.synchronizedList(new ArrayList<Telefone>());

	public void addTelefone(Telefone telefone) {
		telefones.add(telefone);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
}
