package adventure.rest.data;

import adventure.entity.RegistrationStatusType;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

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

    public CategoryData category;

    public TeamData team;
}
