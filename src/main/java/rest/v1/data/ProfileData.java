package rest.v1.data;

import br.gov.frameworkdemoiselle.util.Strings;
import br.gov.frameworkdemoiselle.validation.annotation.Cpf;
import core.entity.GenderType;
import core.entity.TshirtType;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

import static core.util.Constants.*;

@JsonPropertyOrder({"name", "shortName", "gender", "birthday", "tshirt", "city", "mobile", "pendencies",
        "rg", "cpf", "orienteering"})
public class ProfileData {

    @NotEmpty
    @Size(max = NAME_SIZE)
    public String name;

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

    @Length(max = RG_SIZE)
    public String rg;

    @Cpf
    @Length(max = CPF_SIZE)
    public String cpf;

    @JsonProperty("orienteering")
    public OrienteeringData orienteeringData;

    @JsonProperty("short_name")
    public String getShortName() {
        return !Strings.isEmpty(name) ? name.split(" ")[0] : null;
    }
}