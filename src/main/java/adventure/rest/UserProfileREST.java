package adventure.rest;

import static adventure.util.Constants.CPF_SIZE;
import static adventure.util.Constants.NAME_SIZE;
import static adventure.util.Constants.RG_SIZE;
import static adventure.util.Constants.TELEPHONE_SIZE;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.GenderType;
import adventure.entity.Profile;
import adventure.entity.User;
import adventure.persistence.CityDAO;
import adventure.persistence.ProfileDAO;
import adventure.rest.LocationREST.CityData;
import adventure.util.PendencyCounter;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("user/profile")
public class UserProfileREST {

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
		@Size(max = NAME_SIZE)
		public String name;

		@NotEmpty
		@Length(max = RG_SIZE)
		public String rg;

		// @CPF
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
		public GenderType gender;

		@Valid
		@NotNull
		public CityData city;

		private Integer pendencies;

		public Integer getPendencies() {
			return pendencies;
		}
	}
}
