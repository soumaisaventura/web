package adventure.rest.data;

import adventure.entity.GenderType;
import adventure.entity.User;
import adventure.rest.UserHealthREST;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;

@JsonPropertyOrder({"id", "name", "gender", "email", "picture", "mobile", "amount", "kit", "city", "roles", "pendencies"})
public class UserData {

    @NotNull
    public Integer id;

    public String email;

    public BigDecimal amount;

    @Valid
    public KitData kit;

    public RolesData roles;

    public ProfileData profile;

    public HealthData health;

    @JsonIgnore
    private UriInfo uriInfo;

    private PictureData picture;

    public UserData() {
    }

    public UserData(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public UserData(User user, UriInfo uriInfo) {
        this(uriInfo);
        this.id = user.getId();

        if (user.getProfile() != null) {
            this.profile = new ProfileData();
            this.profile.name = user.getProfile().getName();
            this.profile.gender = user.getProfile().getGender();
            this.profile.mobile = user.getProfile().getMobile();
            this.profile.pendencies = user.getProfile().getPendencies();
        }

        if (user.getHealth() != null && user.getHealth().getPendencies() != null) {
            this.health = new HealthData();
            this.health.pendencies = user.getHealth().getPendencies();
        }

        this.roles = new RolesData();
        this.roles.admin = user.getAdmin();
        this.roles.organizer = user.getOrganizer();
    }

    public PictureData getPicture() {
        if (picture == null) {
            picture = new PictureData(this, uriInfo);
        }

        return picture;
    }
}
