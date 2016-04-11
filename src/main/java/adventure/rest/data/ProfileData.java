package adventure.rest.data;

import adventure.entity.GenderType;
import adventure.entity.TshirtType;
import br.gov.frameworkdemoiselle.validation.annotation.Cpf;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

import static adventure.util.Constants.*;

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

    @Past
    @NotNull
    public Date birthday;

    @NotEmpty
    @Length(max = TELEPHONE_SIZE)
    public String mobile;

    @NotNull
    public TshirtType tshirt;

    @NotNull
    public GenderType gender;

    @Valid
    @NotNull
    public CityData city;

    public Integer pendencies;
}