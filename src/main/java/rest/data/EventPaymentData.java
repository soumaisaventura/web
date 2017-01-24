package rest.data;

import core.entity.EventPaymentType;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"type", "info"})
public class EventPaymentData {

    public EventPaymentType type;

    public String info;
}
