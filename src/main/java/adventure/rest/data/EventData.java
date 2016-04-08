package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@JsonPropertyOrder({"id", "internal_id", "status", "name", "description", "banner", "site", "period", "location",
        "payment", "races", "organizers"})
public class EventData {

    public String id;

    @JsonProperty("internal_id")
    public Integer internalId;

    public String status;

    public String name;

    public String description;

    public String site;

    public PeriodData period;

    public LocationData location;

    public EventPaymentData payment;

    public List<RaceData> races;

    public List<UserData> organizers;

    @JsonIgnore
    private UriInfo uriInfo;

    public EventData(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public URI getBanner() {
        return this.id != null ? uriInfo.getBaseUri().resolve("../evento/" + this.id + "/banner.png") : null;
    }
}
