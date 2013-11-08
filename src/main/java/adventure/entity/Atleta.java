package adventure.entity;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

public class Atleta {

	private Long id;

	@NotBlank
	private String nome;

	@NotBlank
	private String email;

	private Date nascimento;

	private String rg;

	private String cpf;

	@Valid
	@NotEmpty
	private Map<TipoTelefone, Telefone> telefones = Collections.synchronizedMap(new HashMap<TipoTelefone, Telefone>());

	public void addTelefone(Telefone telefone, TipoTelefone tipo) {
		telefones.put(tipo, telefone);
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

	public Map<TipoTelefone, Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(Map<TipoTelefone, Telefone> telefones) {
		this.telefones = telefones;
	}
}
