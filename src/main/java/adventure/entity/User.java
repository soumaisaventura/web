package adventure.entity;

import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.*;
import java.security.Principal;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "user_account")
public class User implements Principal {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "seq_user")
    @SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    private String email;

    private String password;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_request")
    private Date passwordResetRequest;

    @Column(name = "facebook_id")
    private String facebookId;

    @Column(name = "facebook_token")
    private String facebookToken;

    @Column(name = "google_id")
    private String googleId;

    @Column(name = "google_token")
    private String googleToken;

    private Date creation;

    private Date activation;

    @Column(name = "activation_token")
    private String activationToken;

    private Date deleted;

    private Boolean admin;

    @Column(name = "_organizer")
    private Boolean organizer;

    @Transient
    private Kit kit;

    @Transient
    private Profile profile;

    @Transient
    private Health health;

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
                Integer healthPendencies, Boolean admin, Boolean organizer) {
        setId(id);
        setEmail(email);
        setAdmin(admin);
        setOrganizer(organizer);

        setProfile(new Profile());
        getProfile().setName(profileName);
        getProfile().setGender(profileGender);
        getProfile().setPendencies(profilePendencies);

        setHealth(new Health());
        getHealth().setPendencies(healthPendencies);
    }

    public User(Integer id, String email, String password, Date activation, String activationToken,
                Date passwordResetRequest, String passwordResetToken, String profileName, GenderType profileGender,
                Integer profilePendencies, Integer healthPendencies, Boolean admin, Boolean organizer) throws Exception {
        setId(id);
        setEmail(email);
        setPassword(password);
        setActivation(activation);
        setActivationToken(activationToken);
        setPasswordResetRequest(passwordResetRequest);
        setPasswordResetToken(passwordResetToken);
        setAdmin(admin);
        setOrganizer(organizer);

        setProfile(new Profile());
        getProfile().setName(profileName);
        getProfile().setGender(profileGender);
        getProfile().setPendencies(profilePendencies);

        setHealth(new Health());
        getHealth().setPendencies(healthPendencies);
    }

    public static User getLoggedIn() {
        return (User) Beans.getReference(SecurityContext.class).getUser();
    }

    @Override
    @Transient
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
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

    public Boolean getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Boolean organizer) {
        this.organizer = organizer;
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

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }
}
