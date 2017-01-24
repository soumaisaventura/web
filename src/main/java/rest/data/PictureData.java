package rest.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.ws.rs.core.UriInfo;

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

    public String getPhoto() {
        return userData.id != null ? uriInfo.getBaseUri().resolve("../usuario/" + userData.id + "/foto.jpg").getPath() : null;
    }

    public String getThumbnail() {
        return userData.id != null ? uriInfo.getBaseUri().resolve("../usuario/" + userData.id + "/minifoto.jpg").getPath() : null;
    }
}
