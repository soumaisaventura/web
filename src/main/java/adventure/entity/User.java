package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;

@Entity
@Table(name = "USER_ACCOUNT", uniqueConstraints = { @UniqueConstraint(name = "UK_USER_EMAIL", columnNames = { "EMAIL" }) })
public class User implements Principal, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "SEQ_USER")
	@SequenceGenerator(name = "SEQ_USER", sequenceName = "SEQ_USER", allocationSize = 1)
	@Column(name = "ID")
	private Long id;

	@Email
	@NotEmpty
	@Index(name = "IDX_USER_EMAIL")
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
	@Index(name = "IDX_USER_DELETED")
	private Date deleted;

	@Transient
	private Profile profile;

	@Transient
	private Health health;

	public static User getLoggedIn() {
		return (User) Beans.getReference(SecurityContext.class).getUser();
	}

	public static User parse(User user) {
		Long id = user.getId();
		String email = user.getEmail();
		String name = user.getProfile().getName();
		Gender gender = user.getProfile().getGender();

		return new User(id, email, name, gender);
	}

	public User() {
	}

	public User(Long id, Gender gender) {
		this.id = id;
		this.profile = new Profile();
		this.profile.setGender(gender);
	}

	public User(Long id, String email, String name, Gender gender) {
		this.id = id;
		this.email = email;

		if (getProfile() == null) {
			setProfile(new Profile());
		}

		getProfile().setName(name);
		getProfile().setGender(gender);
	}

	@Override
	@Transient
	public String getName() {
		return getProfile() != null ? getProfile().getName() : null;
	}

	@Transient
	public Gender getGender() {
		return getProfile() != null ? getProfile().getGender() : null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

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

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Health getHealth() {
		return health;
	}

	public void setHealth(Health health) {
		this.health = health;
	}
}
