package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "ACCOUNT", uniqueConstraints = { @UniqueConstraint(name = "UK_ACCOUNT_EMAIL", columnNames = { "EMAIL" }) })
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@Email
	@NotEmpty
	@Index(name = "IDX_ACCOUNT_EMAIL")
	@Column(name = "EMAIL")
	private String email;

	@JsonIgnore
	@Column(name = "PASSWORD")
	private String password;

	@JsonIgnore
	@Column(name = "PASSWORD_RESET_TOKEN")
	private String passwordResetToken;

	@JsonIgnore
	@Column(name = "PASSWORD_RESET_REQUEST")
	private Date passwordResetRequest;

	@NotNull
	@Temporal(DATE)
	@Column(name = "CREATION")
	private Date creation;

	@JsonIgnore
	@Temporal(DATE)
	@Column(name = "CONFIRMATION")
	private Date confirmation;

	@JsonIgnore
	@Column(name = "CONFIRMATION_TOKEN")
	private String confirmationToken;

	@JsonIgnore
	@Temporal(DATE)
	@Column(name = "DELETED")
	@Index(name = "IDX_ACCOUNT_DELETED")
	private Date deleted;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public Date getPasswordResetRequest() {
		return passwordResetRequest;
	}

	public void setPasswordResetRequest(Date passwordResetRequest) {
		this.passwordResetRequest = passwordResetRequest;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(Date confirmation) {
		this.confirmation = confirmation;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}
}
