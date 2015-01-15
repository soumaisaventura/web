package adventure.rest;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import adventure.entity.User;
import adventure.entity.BloodType;
import adventure.entity.Health;
import adventure.persistence.UserDAO;
import adventure.persistence.HealthDAO;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("health")
public class HealthREST {

	@Inject
	private UserDAO userDAO;

	@GET
	@LoggedIn
	@Produces("application/json")
	public HealthData load() throws Exception {
		Health health = userDAO.loadFull(User.getLoggedIn().getEmail()).getHealth();

		HealthData data = new HealthData();
		data.bloodType = health.getBloodType();
		data.allergy = health.getAllergy();

		return data;
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void update(HealthData data) throws Exception {
		Health health = userDAO.loadFull(User.getLoggedIn().getEmail()).getHealth();
		health.setBloodType(data.bloodType);
		health.setAllergy(data.allergy);
		health.setPendent(false);

		Beans.getReference(HealthDAO.class).update(health);
	}

	public static class HealthData {

		@NotNull
		public BloodType bloodType;

		public String allergy;
	}
}
