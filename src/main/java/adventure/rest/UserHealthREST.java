package adventure.rest;

import adventure.entity.BloodType;
import adventure.entity.Health;
import adventure.entity.User;
import adventure.persistence.HealthDAO;
import adventure.util.PendencyCounter;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.*;

import static adventure.util.Constants.*;

@Path("user/health")
public class UserHealthREST {

    @GET
    @LoggedIn
    @Produces("application/json")
    public HealthData load() throws Exception {
        Health persisted = HealthDAO.getInstance().load(User.getLoggedIn());

        HealthData data = new HealthData();
        data.bloodType = persisted.getBloodType();
        data.allergy = persisted.getAllergy();
        data.pendencies = persisted.getPendencies();
        data.healthCareName = persisted.getHealthCareName();
        data.healthCareNumber = persisted.getHealthCareNumber();
        data.emergencyContactName = persisted.getEmergencyContactName();
        data.emergencyContactPhoneNumber = persisted.getEmergencyContactPhoneNumber();

        return data;
    }

    @PUT
    @LoggedIn
    @Transactional
    @ValidatePayload
    @Consumes("application/json")
    public void update(HealthData data) throws Exception {
        Health persisted = HealthDAO.getInstance().load(User.getLoggedIn());
        persisted.setBloodType(data.bloodType);
        persisted.setAllergy(data.allergy);
        persisted.setHealthCareName(data.healthCareName);
        persisted.setHealthCareNumber(data.healthCareNumber);
        persisted.setEmergencyContactName(data.emergencyContactName);
        persisted.setEmergencyContactPhoneNumber(data.emergencyContactPhoneNumber);

        HealthDAO.getInstance().update(persisted);
        User.getLoggedIn().setHealth(new Health());
        User.getLoggedIn().getHealth().setPendencies(PendencyCounter.count(persisted));
    }

    public static class HealthData {

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

        private Integer pendencies;

        public Integer getPendencies() {
            return pendencies;
        }
    }
}
