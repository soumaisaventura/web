package adventure.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.frameworkdemoiselle.security.User;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@NotEmpty
	private String nome;

	@Email
	@NotEmpty
	@Column(unique = true)
	private String email;

	@NotEmpty
	private String senha;

	@Past
	@NotNull
	private Date nascimento;

	@NotNull
	@Enumerated(STRING)
	private Sexo sexo;

	public Usuario() {
	}

	public Usuario(User user) {
		this.email = user.getId();
		this.id = (Long) user.getAttribute("id");
	}
	
	public User parse() {
		User user = new User() {

			private static final long serialVersionUID = 1L;

			private Map<Object, Object> attrs = new HashMap<Object, Object>();

			@Override
			public String getId() {
				return email;
			}

			@Override
			public Object getAttribute(Object key) {
				return this.attrs.get(key);
			}

			@Override
			public void setAttribute(Object key, Object value) {
				this.attrs.put(key, value);
			}
		};
		user.setAttribute("id", this.id);
		
		return user;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}
}
