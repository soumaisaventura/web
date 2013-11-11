package adventure.entity;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;

import java.util.Date;

import javax.validation.Valid;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

public class Atleta {

	private Long id;

	@NotBlank
	private String email;

	@NotBlank
	private String nome;

	// @NotBlank
	private Date nascimento;

	// @NotBlank
	private String rg;

	// @NotBlank
	private String cpf;

	@Valid
	@JsonSerialize(include = NON_NULL)
	private Telefone telefoneCelular;

	@Valid
	@JsonSerialize(include = NON_NULL)
	private Telefone telefoneResidencial;

	@Valid
	@JsonSerialize(include = NON_NULL)
	private Telefone telefoneComercial;

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
}
