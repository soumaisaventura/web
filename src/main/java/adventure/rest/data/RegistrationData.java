package adventure.rest.data;

import adventure.entity.RegistrationStatusType;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonPropertyOrder({"id", "number", "status", "date", "payment", "submitter", "race", "category", "team"})
public class RegistrationData {

    public Long id;

    public String number;

    public RegistrationStatusType status;

    public Date date;

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
