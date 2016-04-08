package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

@JsonPropertyOrder({"photo", "thumbnail"})
public class PictureData {

    @JsonIgnore
    private UserData userData;

    @JsonIgnore
    private UriInfo uriInfo;

    public PictureData(UserData userData, UriInfo uriInfo) {
        this.userData = userData;
        this.uriInfo = uriInfo;
    }

    public URI getPhoto() {
        return userData.id != null ? uriInfo.getBaseUri().resolve("../usuario/" + userData.id + "/foto.jpg") : null;
    }

    public URI getThumbnail() {
        return userData.id != null ? uriInfo.getBaseUri().resolve("../usuario/" + userData.id + "/minifoto.jpg") : null;
    }
}
