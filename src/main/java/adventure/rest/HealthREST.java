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
import adventure.persistence.HealthDAO;
import adventure.security.User;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("health")
public class HealthREST {

	@Inject
	private HealthDAO dao;

	@GET
	@LoggedIn
	@Produces("application/json")
	public HealthData load() throws Exception {
		Health health = dao.load(User.getLoggedIn().getEmail());

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
		Health health = dao.load(User.getLoggedIn().getEmail());

		health.setBloodType(data.bloodType);
		
		health.setAllergy(data.allergy);

		dao.update(health);
	}

	public static class HealthData {

		@NotNull
		public BloodType bloodType;
		
		public String allergy;
	}
}
