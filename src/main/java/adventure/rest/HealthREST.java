package adventure.rest;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import adventure.entity.BloodType;
import adventure.entity.Health;
import adventure.entity.User;
import adventure.persistence.HealthDAO;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("health")
public class HealthREST {

	@Inject
	private HealthDAO healthDAO;

	@GET
	@LoggedIn
	@Produces("application/json")
	public HealthData load() throws Exception {
		Health persisted = healthDAO.load(User.getLoggedIn());

		HealthData data = new HealthData();
		data.bloodType = persisted.getBloodType();
		data.allergy = persisted.getAllergy();
		data.pendent = persisted.isPendent();

		return data;
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void update(HealthData data) throws Exception {
		Health persisted = healthDAO.load(User.getLoggedIn());
		persisted.setBloodType(data.bloodType);
		persisted.setAllergy(data.allergy);
		persisted.setPendent(false);

		healthDAO.update(persisted);
	}

	public static class HealthData {

		@NotNull
		public BloodType bloodType;

		public String allergy;

		public boolean pendent;
	}
}
