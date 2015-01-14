package adventure.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "PROFILE")
public class Profile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@OneToOne
	@JoinColumn(name = "ID")
	@ForeignKey(name = "FK_PROFILE_ACCOUNT")
	private Account account;

	@NotEmpty
	@Column(name = "NAME")
	@Index(name = "IDX_PROFILE_NAME")
	private String name;

	@Length(max = 10)
	@Column(name = "RG")
	private String rg;

	@Length(max = 11)
	@Column(name = "CPF")
	@Index(name = "IDX_PROFILE_CPF")
	private String cpf;

	@Past
	@Temporal(DATE)
	@Column(name = "BIRTHDAY")
	private Date birthday;

	@NotNull
	@Enumerated(STRING)
	@Column(name = "GENDER")
	private Gender gender;

	@Column(name = "PENDENT", nullable = false)
	private boolean pendent = true;

	public Profile() {
	}

	public Profile(Account account) {
		this.account = account;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Profile)) {
			return false;
		}
		Profile other = (Profile) obj;
		if (account == null) {
			if (other.account != null) {
				return false;
			}
		} else if (!account.equals(other.account)) {
			return false;
		}
		return true;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public boolean isPendent() {
		return pendent;
	}

	public void setPendent(boolean pendent) {
		this.pendent = pendent;
	}
}
