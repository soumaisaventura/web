package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.BloodType;
import adventure.entity.Gender;
import adventure.entity.JSEntity;
import adventure.entity.User;
import adventure.persistence.UserDAO;
import adventure.persistence.ValidationException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.security.SecurityContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ValidateRequest
@Path("/api/register")
@Produces(APPLICATION_JSON)
public class RegisterService {

	@Inject
	private SecurityContext securityContext;

	@Inject
	private UserDAO dao;

	@GET
	@LoggedIn
	@Path("/personal")
	public PersonalForm loadPersonal() throws Exception {
		PersonalForm form = new PersonalForm();
		User user = dao.load(new User(securityContext.getUser()).getId());
		BeanUtils.copyProperties(form, user);

		return form;
	}

	@GET
	@LoggedIn
	@Path("/medical")
	public MedicalForm loadMedical() throws Exception {
		MedicalForm form = new MedicalForm();
		User user = dao.load(new User(securityContext.getUser()).getId());
		BeanUtils.copyProperties(form, user);

		return form;
	}

	@PUT
	@LoggedIn
	@Transactional
	@Path("/personal")
	public void updatePersonal(@NotNull @Valid PersonalForm form) throws Exception {
		User user = dao.load(new User(securityContext.getUser()).getId());
		BeanUtils.copyProperties(user, form);

		dao.update(user);
	}

	@PUT
	@LoggedIn
	@Transactional
	@Path("/medical")
	public void updateMedical(@NotNull @Valid MedicalForm form) throws Exception {
		User user = dao.load(new User(securityContext.getUser()).getId());
		BeanUtils.copyProperties(user, form);

		dao.update(user);
	}

	@POST
	@LoggedIn
	@Path("/{eventId}")
	@Transactional
	public void register(@NotEmpty @PathParam("eventId") String eventId) throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		boolean valid = true;
		valid &= validator.validate(loadPersonal()).isEmpty();
		valid &= validator.validate(loadMedical()).isEmpty();

		if (valid) {
			// TODO Associar usuário ao evento (eventId).

		} else {
			ValidationException exception = new ValidationException();
			exception.addViolation(null, "Existem dados cadastrais pendentes");

			throw exception;
		}
	}

	@JSEntity
	public static class PersonalForm {

		@NotEmpty
		private String fullName;

		@NotNull
		private Gender gender;

		@NotEmpty
		private String rg;

		@NotEmpty
		private String cpf;

		public String getFullName() {
			return fullName;
		}

		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public Gender getGender() {
			return gender;
		}

		public void setGender(Gender gender) {
			this.gender = gender;
		}

		public String getRg() {
			return rg;
		}

		public void setRg(String rg) {
			this.rg = rg;
		}

		public String getCpf() {
			return cpf;
		}

		public void setCpf(String cpf) {
			this.cpf = cpf;
		}
	}

	@JSEntity
	public static class MedicalForm {

		@NotNull
		private BloodType bloodType;

		public BloodType getBloodType() {
			return bloodType;
		}

		public void setBloodType(BloodType bloodType) {
			this.bloodType = bloodType;
		}
	}
}
