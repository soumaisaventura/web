package rest.v1.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"admin", "organizer"})
public class RolesData {

    public Boolean admin;

    public Boolean organizer;
}
