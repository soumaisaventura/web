package rest.v1;

import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import core.entity.Health;
import core.entity.User;
import core.persistence.HealthDAO;
import rest.v1.data.HealthData;
import temp.util.PendencyCounter;

import javax.ws.rs.*;

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
}
