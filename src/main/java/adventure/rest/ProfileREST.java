package adventure.rest;

import java.util.Date;

import javax.inject.Inject;
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
import adventure.persistence.AccountDAO;
import adventure.persistence.ProfileDAO;
import adventure.security.User;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("profile")
public class ProfileREST {

	@Inject
	private AccountDAO accountDAO;

	@GET
	@LoggedIn
	@Produces("application/json")
	public ProfileData load() throws Exception {
		Profile profile = accountDAO.loadFull(User.getLoggedIn().getEmail()).getProfile();

		ProfileData data = new ProfileData();
		data.name = profile.getName();
		data.rg = profile.getRg();
		data.cpf = profile.getCpf();
		data.birthday = profile.getBirthday();
		data.gender = profile.getGender();

		return data;
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void update(ProfileData data) throws Exception {
		Profile profile = accountDAO.loadFull(User.getLoggedIn().getEmail()).getProfile();
		profile.setName(data.name);
		profile.setRg(data.rg);
		profile.setCpf(data.cpf);
		profile.setBirthday(data.birthday);
		profile.setGender(data.gender);
		profile.setPendent(false);

		Beans.getReference(ProfileDAO.class).update(profile);
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
	}
}
