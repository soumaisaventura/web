package adventure.rest.data;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"code", "transaction"})
public class RegistrationPaymentData {

    public String code;

    public String transaction;
}
