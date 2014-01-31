package adventure.rest.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Athlete;
import adventure.entity.BloodType;
import adventure.entity.JSEntity;
import adventure.persistence.PersonalDataDAO;

@Path("/api/register")
public class RegisterService {

	@Inject
	private PersonalDataDAO dao;

	@GET
	@Path("/personal")
	public PersonalForm loadPersonal() {
		return null;
	}

	@PUT
	@Path("/personal")
	public void updatePersonal(@NotNull @Valid PersonalForm form) {

	}

	@GET
	@Path("/medical")
	public MedicalForm loadMedical() {
		return null;
	}

	@PUT
	@Path("/medical")
	public void updateMedical(@NotNull @Valid MedicalForm form) {

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void insert(Athlete personalData) throws Exception {
		dao.insert(personalData);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Athlete> all() {
		return dao.findAll();
	}

	@JSEntity
	static class PersonalForm {

		@NotEmpty
		private String rg;

		@NotEmpty
		private String cpf;

		public String getRg() {
			return rg;
		}

		public void setRg(String rg) {
			this.rg = rg;
		}

		public String getCpf() {Personal
			return cpf;
		}

		public void setCpf(String cpf) {
			this.cpf = cpf;
		}
	}

	@JSEntity
	static class MedicalForm {

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
