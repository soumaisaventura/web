package adventure.entity;

import java.util.Date;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

public class Atleta {

	private Long id;

	@NotBlank
	private String email;

	@NotBlank
	private String nome;

	@NotBlank
	private Date nascimento;

	@NotBlank
	private String rg;

	@NotBlank
	private String cpf;

	@Valid
	private Telefone telefoneCelular;

	@Valid
	private Telefone telefoneResidencial;

	@Valid
	private Telefone telefoneComercial;

	// @Valid
	// @NotEmpty
	// private Map<TipoTelefone, Telefone> telefones = Collections.synchronizedMap(new HashMap<TipoTelefone,
	// Telefone>());

	// public void addTelefone(Telefone telefone, TipoTelefone tipo) {
	// telefones.put(tipo, telefone);
	// }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public Telefone getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setTelefoneCelular(Telefone telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	public Telefone getTelefoneResidencial() {
		return telefoneResidencial;
	}

	public void setTelefoneResidencial(Telefone telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}

	public Telefone getTelefoneComercial() {
		return telefoneComercial;
	}

	public void setTelefoneComercial(Telefone telefoneComercial) {
		this.telefoneComercial = telefoneComercial;
	}

	// public Map<TipoTelefone, Telefone> getTelefones() {
	// return telefones;
	// }
	//
	// public void setTelefones(Map<TipoTelefone, Telefone> telefones) {
	// this.telefones = telefones;
	// }
}
