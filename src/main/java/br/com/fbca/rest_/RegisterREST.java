package br.com.fbca.rest_;

import javax.inject.Inject;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.fbca.entity.BloodType;
import br.com.fbca.entity.Gender;
import br.com.fbca.entity.User;
import br.com.fbca.persistence.UserDAO;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("register")
public class RegisterREST {

	@Inject
	private SecurityContext securityContext;

	@Inject
	private UserDAO dao;

	@GET
	@LoggedIn
	@Path("/personal")
	@Produces("application/json")
	public PersonalData loadPersonal() throws Exception {
		PersonalData data = new PersonalData();
		User user = dao.load(((User) securityContext.getUser()).getId());
		BeanUtils.copyProperties(data, user);

		return data;
	}

	@GET
	@LoggedIn
	@Path("/medical")
	@Produces("application/json")
	public MedicalData loadMedical() throws Exception {
		MedicalData data = new MedicalData();
		User user = dao.load(((User) securityContext.getUser()).getId());
		BeanUtils.copyProperties(data, user);

		return data;
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Path("/personal")
	@Consumes("application/json")
	@Produces("application/json")
	public void updatePersonal(PersonalData data) throws Exception {
		User user = dao.load(((User) securityContext.getUser()).getId());
		BeanUtils.copyProperties(user, data);

		dao.update(user);
	}

	@PUT
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Path("/medical")
	@Consumes("application/json")
	public void updateMedical(MedicalData data) throws Exception {
		User user = dao.load(((User) securityContext.getUser()).getId());
		BeanUtils.copyProperties(user, data);

		dao.update(user);
	}

	@POST
	@LoggedIn
	@Transactional
	@Path("/{eventId}")
	public void register(@PathParam("eventId") String eventId) throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		boolean valid = true;
		valid &= validator.validate(loadPersonal()).isEmpty();
		valid &= validator.validate(loadMedical()).isEmpty();

		if (valid) {
			// TODO Associar usuário ao evento (eventId).

		} else {
			throw new UnprocessableEntityException().addViolation("Existem dados cadastrais pendentes");
		}
	}

	public static class PersonalData {

		@NotEmpty
		String name;

		@NotNull
		Gender gender;

		@NotEmpty
		String rg;

		@NotEmpty
		String cpf;
	}

	public static class MedicalData {

		@NotNull
		BloodType bloodType;
	}
}
