package rest.data;

import core.entity.RegistrationStatusType;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonPropertyOrder({"id", "number", "status", "date", "referencePrice", "payment", "submitter", "race", "category", "team"})
public class RegistrationData {

    public Long id;

    public String number;

    public RegistrationStatusType status;

    public Date date;

    @JsonProperty("reference_price")
    public PeriodData referencePrice;

    public RegistrationPaymentData payment;

    public UserData submitter;

    public RaceData race;

    @Valid
    @NotNull
    public CategoryData category;

    @Valid
    @NotNull
    public TeamData team;
}
