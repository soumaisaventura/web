package adventure.rest.data;

import adventure.entity.GenderType;
import adventure.entity.User;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;

@JsonPropertyOrder({"id", "name", "gender", "email", "picture", "mobile", "racePrice", "city", "roles", "pendencies"})
public class UserData {

    public Integer id;

    public String name;

    public GenderType gender;

    public String email;

    public String mobile;

    @JsonProperty("race_price")
    public BigDecimal racePrice;

    public CityData city;

    public RolesData roles;

    public PendenciesData pendencies;

    @JsonIgnore
    private UriInfo uriInfo;

    private PictureData picture;

    public UserData(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public UserData(User user, UriInfo uriInfo) {
        this(uriInfo);

        this.id = user.getId();
        this.name = user.getProfile().getName();
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
