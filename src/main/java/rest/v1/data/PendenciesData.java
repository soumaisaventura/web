package rest.v1.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"profile", "health"})
public class PendenciesData {

    public Integer profile;

    public Integer health;
}
