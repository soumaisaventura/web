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

	// private String sessionToken;

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
	@Column(name = "ACTIVATION")
	private Date activation;

	@JsonIgnore
	@Column(name = "ACTIVATION_TOKEN")
	private String activationToken;

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

	public User() {
	}

	public User(Long id, String email, String profileName, Gender profileGender) {
		setId(id);
		setEmail(email);

		setProfile(new Profile());
		getProfile().setName(profileName);
		getProfile().setGender(profileGender);
	}

	public User(Long id, String email, String profileName, Gender profileGender, boolean profilePendent,
			boolean healthPendent) {
		setId(id);
		setEmail(email);

		setProfile(new Profile());
		getProfile().setName(profileName);
		getProfile().setGender(profileGender);
		getProfile().setPendent(profilePendent);

		setHealth(new Health());
		getHealth().setPendent(healthPendent);
	}

	public User(Long id, String email, String password, Date activation, String activationToken, String profileName,
			Gender profileGender, boolean profilePendent, boolean healthPendent) throws Exception {
		setId(id);
		setEmail(email);
		setPassword(password);
		setActivation(activation);
		setActivationToken(activationToken);

		setProfile(new Profile());
		getProfile().setName(profileName);
		getProfile().setGender(profileGender);
		getProfile().setPendent(profilePendent);

		setHealth(new Health());
		getHealth().setPendent(healthPendent);
	}

	@Override
	@Transient
	@JsonIgnore
	public String getName() {
		return getProfile() != null ? getProfile().getName() : null;
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

	public Date getActivation() {
		return activation;
	}

	public void setActivation(Date activation) {
		this.activation = activation;
	}

	public String getActivationToken() {
		return activationToken;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
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
