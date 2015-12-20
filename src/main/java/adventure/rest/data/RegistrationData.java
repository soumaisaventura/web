package adventure.rest.data;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import adventure.entity.RegistrationStatusType;

@JsonPropertyOrder({ "id", "number", "status", "date", "payment", "submitter", "race", "category", "team" })
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
