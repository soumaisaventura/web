package adventure.rest;

import adventure.business.ImageBusiness;
import adventure.business.ProfileBusiness;
import adventure.entity.GenderType;
import adventure.entity.Profile;
import adventure.entity.TshirtType;
import adventure.entity.User;
import adventure.persistence.CityDAO;
import adventure.persistence.ProfileDAO;
import adventure.rest.LocationREST.CityData;
import adventure.util.PendencyCounter;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Cache;
import br.gov.frameworkdemoiselle.util.ValidatePayload;
import br.gov.frameworkdemoiselle.validation.annotation.Cpf;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static adventure.util.Constants.*;

@Path("user/profile")
public class UserProfileREST {

	@GET
	@LoggedIn
	@Produces("application/json")
	public ProfileData load() throws Exception {
		Profile profile = loadProfileDetails(User.getLoggedIn());

		ProfileData data = new ProfileData();
		data.name = profile.getName();
		data.rg = profile.getRg();
		data.cpf = profile.getCpf();
		data.birthday = profile.getBirthday();
		data.mobile = profile.getMobile();
		data.gender = profile.getGender();
		data.tshirt = profile.getTshirt();
		data.pendencies = profile.getPendencies();
		data.city = new CityData();
		data.city.id = profile.getCity().getId();
		data.city.name = profile.getCity().getName();
		data.city.state = profile.getCity().getState().getAbbreviation();

		return data;
	}

	@GET
	@Path("{id: \\d+}/picture")
	@Produces("image/jpeg")
	@Cache("max-age=604800000")
	public byte[] getPicture(@PathParam("id") Integer id, @Context ServletContext context) throws Exception {
		Profile profile = loadProfile(id);
		return loadPicture(profile, context);
	}

	@GET
	@Path("{id: \\d+}/thumbnail")
	@Produces("image/jpeg")
	@Cache("max-age=604800000")
	public byte[] getThumbnail(@PathParam("id") Integer id, @Context ServletContext context) throws Exception {
		Profile profile = loadProfile(id);
		return ImageBusiness.getInstance()
				.resize(loadPicture(profile, context), USER_PHOTO_WIDTH, USER_THUMBNAIL_WIDTH);
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Path("{id: \\d+}/picture")
	@Consumes("multipart/form-data")
	public void setPicture(@PathParam("id") Integer id, @NotEmpty MultipartFormDataInput input) throws Exception {
		Profile profile = loadProfile(id);
		checkPermission(profile);

		InputPart file = null;
		Map<String, List<InputPart>> formDataMap = input.getFormDataMap();

		for (Map.Entry<String, List<InputPart>> entry : formDataMap.entrySet()) {
			file = entry.getValue().get(0);
			break;
		}

		if (file == null) {
			throw new UnprocessableEntityException().addViolation("file", "campo obrigatório");
		}

		InputStream inputStream = file.getBody(InputStream.class, null);
		ProfileBusiness.getInstance().updatePicture(id, inputStream);
	}

	private void checkPermission(Profile profile) throws ForbiddenException {
		if (!User.getLoggedIn().getAdmin() && !profile.getUser().getId().equals(User.getLoggedIn().getId())) {
			throw new ForbiddenException();
		}
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Consumes("application/json")
	public void update(ProfileData data) throws Exception {
		Profile persisted = loadProfile(User.getLoggedIn());
		persisted.setName(data.name);
		persisted.setRg(data.rg);
		persisted.setCpf(data.cpf);
		persisted.setBirthday(data.birthday);
		persisted.setMobile(data.mobile);
		persisted.setGender(data.gender);
		persisted.setTshirt(data.tshirt);
		persisted.setCity(CityDAO.getInstance().load(data.city.id));

		ProfileDAO.getInstance().update(persisted);
		User.getLoggedIn().getProfile().setPendencies(PendencyCounter.count(persisted));
	}

	private Profile loadProfile(Integer id) throws NotFoundException {
		Profile result = ProfileDAO.getInstance().load(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Profile loadProfile(User user) throws NotFoundException {
		Profile result = ProfileDAO.getInstance().load(user);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private Profile loadProfileDetails(User user) throws NotFoundException {
		Profile result = ProfileDAO.getInstance().loadDetails(user);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private byte[] loadPicture(Profile profile, ServletContext context) throws Exception {
		byte[] result = profile.getPicture();

		if (result == null) {
			InputStream in = context.getResourceAsStream("/images/foto_anonimo_"
					+ profile.getGender().toString().toLowerCase() + ".jpg");
			result = IOUtils.toByteArray(in);
		}

		return result;
	}

	public static class ProfileData {

		@NotEmpty
		@Size(max = NAME_SIZE)
		public String name;

		@NotEmpty
		@Length(max = RG_SIZE)
		public String rg;

		@Cpf
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
		public TshirtType tshirt;

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
