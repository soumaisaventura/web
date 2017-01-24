package rest.data;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"checkoutCode", "transactionCode"})
public class RegistrationPaymentData {

    @JsonProperty("checkout_code")
    public String checkoutCode;

    @JsonProperty("transaction_code")
    public String transactionCode;
}
