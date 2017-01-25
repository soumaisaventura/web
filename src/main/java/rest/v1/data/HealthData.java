package rest.v1.data;

import core.entity.BloodType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static core.util.Constants.*;

public class HealthData {

    @NotNull
    public BloodType bloodType;

    @Size(max = TEXT_SIZE)
    public String allergy;

    @Size(max = NAME_SIZE)
    public String healthCareName;

    @Size(max = GENERIC_ID_SIZE)
    public String healthCareNumber;

    @NotEmpty
    @Size(max = NAME_SIZE)
    public String emergencyContactName;

    @NotEmpty
    @Size(max = TELEPHONE_SIZE)
    public String emergencyContactPhoneNumber;

    public Integer pendencies;
}