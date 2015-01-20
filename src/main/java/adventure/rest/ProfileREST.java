package adventure.rest;

import java.util.Date;

import javax.validation.Valid;
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
import adventure.persistence.CityDAO;
import adventure.persistence.ProfileDAO;
import adventure.util.PendencyCounter;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("profile")
public class ProfileREST {

	@GET
	@LoggedIn
	@Produces("application/json")
	public ProfileData load() throws Exception {
		Profile profile = ProfileDAO.getInstance().loadDetails(User.getLoggedIn());

		ProfileData data = new ProfileData();
		data.name = profile.getName();
		data.rg = profile.getRg();
		data.cpf = profile.getCpf();
		data.birthday = profile.getBirthday();
		data.mobile = profile.getMobile();
		data.gender = profile.getGender();
		data.pendencies = profile.getPendencies();
		data.city = new CityData();
		data.city.id = profile.getCity().getId();
		data.city.name = profile.getCity().getName();
		data.city.state = profile.getCity().getState().getAbbreviation();

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
		persisted.setMobile(data.mobile);
		persisted.setGender(data.gender);
		persisted.setCity(CityDAO.getInstance().load(data.city.id));

		ProfileDAO.getInstance().update(persisted);
		User.getLoggedIn().getProfile().setPendencies(PendencyCounter.count(persisted));
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

		@NotEmpty
		public String mobile;

		@NotNull
		public Gender gender;

		@Valid
		@NotNull
		public CityData city;

		private Integer pendencies;

		public Integer getPendencies() {
			return pendencies;
		}
	}

	public static class CityData {

		@NotNull(message = "escolha uma cidade listada")
		public Long id;

		public String name;

		public String state;
	}
}
