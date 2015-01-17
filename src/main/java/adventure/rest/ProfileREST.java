package adventure.rest;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;

import adventure.entity.Gender;
import adventure.entity.Profile;
import adventure.entity.User;
import adventure.persistence.ProfileDAO;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("profile")
public class ProfileREST {

	@GET
	@LoggedIn
	@Produces("application/json")
	public ProfileData load() throws Exception {
		Profile persisted = ProfileDAO.getInstance().load(User.getLoggedIn());

		ProfileData data = new ProfileData();
		data.name = persisted.getName();
		data.rg = persisted.getRg();
		data.cpf = persisted.getCpf();
		data.birthday = persisted.getBirthday();
		data.gender = persisted.getGender();
		data.pendent = persisted.isPendent();

		return data;
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void update(ProfileData data) throws Exception {
		Profile persisted = ProfileDAO.getInstance().load(User.getLoggedIn());
		persisted.setName(data.name);
		persisted.setRg(data.rg);
		persisted.setCpf(data.cpf);
		persisted.setBirthday(data.birthday);
		persisted.setGender(data.gender);
		persisted.setPendent(false);

		ProfileDAO.getInstance().update(persisted);
	}

	public static class ProfileData {

		@NotEmpty
		public String name;

		@NotEmpty
		public String rg;

		@CPF
		@NotEmpty
		public String cpf;

		@Past
		@NotNull
		public Date birthday;

		@NotNull
		public Gender gender;

		@NotNull
		public Long city;

		private boolean pendent;

		public boolean isPendent() {
			return pendent;
		}
	}
}
