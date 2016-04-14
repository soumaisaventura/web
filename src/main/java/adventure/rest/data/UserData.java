package adventure.rest.data;

import adventure.entity.GenderType;
import adventure.entity.User;
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

    public String name;

    public GenderType gender;

    public String email;

    public String mobile;

    public BigDecimal amount;

    @Valid
    public KitData kit;

    public CityData city;

    public RolesData roles;

    public PendenciesData pendencies;

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
        this.name = user.getName();
        this.gender = user.getProfile().getGender();
        this.mobile = user.getProfile().getMobile();

        this.pendencies = new PendenciesData();
        this.pendencies.profile = user.getProfile().getPendencies();
        this.pendencies.health = user.getHealth().getPendencies();

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
