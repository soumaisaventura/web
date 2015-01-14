package adventure.rest;

import static adventure.entity.Gender.FEMALE;
import static adventure.entity.Gender.MALE;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.Account;
import adventure.entity.Category;
import adventure.entity.Gender;
import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.entity.Register;
import adventure.entity.TeamFormation;
import adventure.persistence.AccountDAO;
import adventure.persistence.MailDAO;
import adventure.persistence.RaceCategoryDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.RegisterDAO;
import adventure.persistence.TeamFormationDAO;
import adventure.security.User;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transaction;
import br.gov.frameworkdemoiselle.transaction.TransactionContext;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("race/{id}/register")
public class RegisterREST {

	@Inject
	private RegisterDAO registerDAO;

	@Inject
	private TeamFormationDAO teamFormationDAO;

	@POST
	@LoggedIn
	@Transactional
	@ValidatePayload
	@Path("validate")
	@Consumes("application/json")
	@Produces("application/json")
	public void validate(RegisterData data, @PathParam("id") Long id) throws Exception {
		validateData(data, id);
	}

	@POST
	@LoggedIn
	@ValidatePayload
	@Consumes("application/json")
	@Produces("application/json")
	public Long submit(RegisterData data, @PathParam("id") Long id, @Context UriInfo uriInfo) throws Exception {
		Transaction transaction = Beans.getReference(TransactionContext.class).getCurrentTransaction();
		transaction.begin();
		SubmitResult submitResult = null;

		try {
			ValidationResult validationResult = validateData(data, id);
			submitResult = submit(data, validationResult);
			transaction.commit();
		} catch (Exception cause) {
			transaction.rollback();
			throw cause;
		}

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		Beans.getReference(MailDAO.class).sendRegisterCreation(submitResult.register, submitResult.members, baseUri);
		return submitResult.register.getId();
	}

	private SubmitResult submit(RegisterData data, ValidationResult validationResult) {
		SubmitResult result = new SubmitResult();
		Register register = new Register();
		register.setTeamName(data.teamName);
		register.setRaceCategory(validationResult.raceCategory);
		register.setCreator(Beans.getReference(AccountDAO.class).load(User.getLoggedIn().getEmail()));
		result.register = registerDAO.insert(register);

		for (Account member : validationResult.members) {
			Account atachedMember = Beans.getReference(AccountDAO.class).load(member.getId());
			TeamFormation teamFormation = new TeamFormation(register, atachedMember);

			if (member.getId().equals(User.getLoggedIn().getId())) {
				teamFormation.setConfirmed(true);
			}

			teamFormationDAO.insert(teamFormation);
			result.members.add(atachedMember);
		}

		return result;
	}

	private ValidationResult validateData(RegisterData data, Long id) throws Exception {
		ValidationResult result = new ValidationResult();

		loadRace(id);
		result.raceCategory = loadRaceCategory(id, data.course, data.category);
		result.members = loadMembers(data.members);
		validate(result.raceCategory.getCategory(), result.members);

		return result;
	}

	private Race loadRace(Long id) throws Exception {
		Race result = Beans.getReference(RaceDAO.class).load(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private RaceCategory loadRaceCategory(Long raceId, Long courseId, Long categoryId) throws Exception {
		RaceCategory result = Beans.getReference(RaceCategoryDAO.class).loadForRegister(raceId, courseId, categoryId);

		if (result == null) {
			throw new UnprocessableEntityException().addViolation("category", "indisponível para esta prova");
		}

		return result;
	}

	private List<Account> loadMembers(List<Long> ids) throws Exception {
		List<Account> result = new ArrayList<Account>();
		UnprocessableEntityException exception = new UnprocessableEntityException();

		for (Long id : ids) {
			Account account = Beans.getReference(AccountDAO.class).loadGender(id);

			if (account == null) {
				exception.addViolation("members", "usuário inválido");
			} else if (result.contains(account)) {
				exception.addViolation("members", "usuário duplicado");
			} else {
				result.add(account);
			}
		}

		if (!exception.getViolations().isEmpty()) {
			throw exception;
		}

		return result;
	}

	private void validate(Category category, List<Account> members) throws Exception {
		int total = members.size();
		if (total > category.getTeamSize()) {
			throw new UnprocessableEntityException().addViolation("members", "tem muita gente");
		} else if (total < category.getTeamSize()) {
			throw new UnprocessableEntityException().addViolation("members", "equipe incompleta");
		}

		int male = count(members, MALE);
		if (category.getMinMaleMembers() != null && male < category.getMinMaleMembers()) {
			throw new UnprocessableEntityException().addViolation("members", "está faltando macho");
		}

		int female = count(members, FEMALE);
		if (category.getMinFemaleMembers() != null && female < category.getMinFemaleMembers()) {
			throw new UnprocessableEntityException().addViolation("members", "está faltando mulher");
		}
	}

	private int count(List<Account> members, Gender gender) {
		int result = 0;

		for (Account account : members) {
			if (account.getProfile().getGender() == gender) {
				result++;
			}
		}

		return result;
	}

	private class ValidationResult {

		RaceCategory raceCategory;

		List<Account> members = new ArrayList<Account>();

	}

	private class SubmitResult {

		Register register;

		List<Account> members = new ArrayList<Account>();

	}

	public static class RegisterData {

		@NotEmpty
		public String teamName;

		@NotNull
		public Long category;

		@NotNull
		public Long course;

		@NotEmpty
		public List<Long> members;
	}
}
