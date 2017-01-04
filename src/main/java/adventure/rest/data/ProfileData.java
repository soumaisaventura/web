package adventure.rest.data;

import static adventure.util.Constants.CPF_SIZE;
import static adventure.util.Constants.NAME_SIZE;
import static adventure.util.Constants.RG_SIZE;
import static adventure.util.Constants.TELEPHONE_SIZE;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.GenderType;
import adventure.entity.TshirtType;
import br.gov.frameworkdemoiselle.util.Strings;
import br.gov.frameworkdemoiselle.validation.annotation.Cpf;

@JsonPropertyOrder({ "name", "shortName", "rg", "cpf", "gender", "birthday", "tshirt", "city", "mobile", "pendencies",
		"orienteering" })
public class ProfileData {

	@NotEmpty
	@Size(max = NAME_SIZE)
	public String name;

	@NotEmpty
	@Length(max = RG_SIZE)
	public String rg;

	@Cpf
	@NotEmpty
	@Length(max = CPF_SIZE)
	public String cpf;

	@NotNull
	public GenderType gender;

	@Past
	@NotNull
	public Date birthday;

	@NotNull
	public TshirtType tshirt;

	@Valid
	@NotNull
	public CityData city;

	@NotEmpty
	@Length(max = TELEPHONE_SIZE)
	public String mobile;

	public Integer pendencies;

	@JsonProperty("orienteering")
	public OrienteeringData orienteeringData;

	@JsonProperty("short_name")
	public String getShortName() {
		return !Strings.isEmpty(name) ? name.split(" ")[0] : null;
	}

}