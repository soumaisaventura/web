package adventure.entity;

import static adventure.util.Constants.EMAIL_SIZE;
import static adventure.util.Constants.HASH_SIZE;
import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;

@Entity
@Table(name = "user_account", uniqueConstraints = { @UniqueConstraint(name = "uk_user_email", columnNames = { "email" }) })
public class User implements Principal, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_user")
	@SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1)
	@Column(name = "id")
	private Integer id;

	@Email
	@NotEmpty
	@Size(max = EMAIL_SIZE)
	@Column(name = "email")
	@Index(name = "idx_user_email")
	private String email;

	@JsonIgnore
	@Size(max = HASH_SIZE)
	@Column(name = "password")
	private String password;

	@JsonIgnore
	@Size(max = HASH_SIZE)
	@Column(name = "password_reset_token")
	private String passwordResetToken;

	@JsonIgnore
	@Column(name = "password_reset_request")
	private Date passwordResetRequest;

	@NotNull
	@Column(name = "creation")
	private Date creation;

	@JsonIgnore
	@Column(name = "activation")
	private Date activation;

	@JsonIgnore
	@Size(max = HASH_SIZE)
	@Column(name = "activation_token")
	private String activationToken;

	@JsonIgnore
	@Column(name = "deleted")
	@Index(name = "idx_user_deleted")
	private Date deleted;

	@Column(name = "admin")
	@Index(name = "idx_user_admin")
	private Boolean admin;

	@Transient
	private Profile profile;

	@Transient
	private Health health;

	public static User getLoggedIn() {
		return (User) Beans.getReference(SecurityContext.class).getUser();
	}

	public User() {
	}

	public User(Integer id, String email, String profileName, GenderType profileGender, String profileMobile) {
		setId(id);
		setEmail(email);

		setProfile(new Profile());
		getProfile().setName(profileName);
		getProfile().setGender(profileGender);
		getProfile().setMobile(profileMobile);
	}

	public User(Integer id, String email, String profileName, Integer pendencies, Date creation, Date activation) {
		setId(id);
		setEmail(email);

		setProfile(new Profile());
		getProfile().setName(profileName);
	}

	public User(Integer id, String email, String profileName, GenderType profileGender, Integer profilePendencies,
			Integer healthPendencies, Boolean admin) {
		setId(id);
		setEmail(email);
		setAdmin(admin);

		setProfile(new Profile());
		getProfile().setName(profileName);
		getProfile().setGender(profileGender);
		getProfile().setPendencies(profilePendencies);

		setHealth(new Health());
		getHealth().setPendencies(healthPendencies);
	}

	public User(Integer id, String email, String password, Date activation, String activationToken,
			Date passwordResetRequest, String passwordResetToken, String profileName, GenderType profileGender,
			Integer profilePendencies, Integer healthPendencies, Boolean admin) throws Exception {
		setId(id);
		setEmail(email);
		setPassword(password);
		setActivation(activation);
		setActivationToken(activationToken);
		setPasswordResetRequest(passwordResetRequest);
		setPasswordResetToken(passwordResetToken);
		setAdmin(admin);

		setProfile(new Profile());
		getProfile().setName(profileName);
		getProfile().setGender(profileGender);
		getProfile().setPendencies(profilePendencies);

		setHealth(new Health());
		getHealth().setPendencies(healthPendencies);
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
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
