package adventure.rest;

import static adventure.entity.GenderType.FEMALE;
import static adventure.entity.GenderType.MALE;
import static adventure.entity.RegistrationStatusType.CONFIRMED;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import adventure.entity.AnnualFee;
import adventure.entity.Category;
import adventure.entity.City;
import adventure.entity.Course;
import adventure.entity.GenderType;
import adventure.entity.Health;
import adventure.entity.Period;
import adventure.entity.Profile;
import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.entity.RaceOrganizer;
import adventure.entity.Registration;
import adventure.entity.TeamFormation;
import adventure.entity.User;
import adventure.persistence.MailDAO;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.UserDAO;
import adventure.security.Passwords;
import adventure.util.Dates;
import adventure.util.PendencyCounter;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("temp")
public class TempREST {

	@GET
	@Path("date")
	public Date getDate() {
		return new Date();
	}

	@POST
	@Transactional
	@Path("prepare")
	public void prepare(@Context UriInfo uriInfo) throws Exception {
		validate(uriInfo);

		UserDAO userDAO = UserDAO.getInstance();
		for (User user : userDAO.findAll()) {
			if (!user.getAdmin()) {
				user.setEmail("test_" + user.getEmail());
			}
			user.setPassword(Passwords.hash("123", user.getEmail()));
			userDAO.update(user);
		}
	}

	@POST
	@Transactional
	@Path("registration-email")
	@Consumes("application/json")
	public void unloadRegistration(Long registerId, @Context UriInfo uriInfo) throws Exception {
		validate(uriInfo);
		Registration registration = RegistrationDAO.getInstance().loadForDetails(registerId);

		if (registration == null) {
			throw new NotFoundException();
		} else if (registration.getStatus() != CONFIRMED) {
			throw new UnprocessableEntityException();
		}

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailDAO.getInstance().sendRegistrationConfirmation(registration, baseUri);
	}

	@POST
	@Transactional
	@Path("unload/registration")
	public void unloadRegistration(@Context UriInfo uriInfo) throws Exception {
		validate(uriInfo);

		getEntityManager().createQuery("delete from AnnualFeePayment").executeUpdate();
		getEntityManager().createQuery("delete from TeamFormation").executeUpdate();
		getEntityManager().createQuery("delete from Registration").executeUpdate();
	}

	@POST
	@Transactional
	@Path("unload")
	public void unload(@Context UriInfo uriInfo) throws Exception {
		validate(uriInfo);

		getEntityManager().createQuery("delete from AnnualFeePayment").executeUpdate();
		getEntityManager().createQuery("delete from AnnualFee").executeUpdate();
		getEntityManager().createQuery("delete from RaceOrganizer").executeUpdate();
		getEntityManager().createQuery("delete from TeamFormation").executeUpdate();
		getEntityManager().createQuery("delete from Registration").executeUpdate();
		getEntityManager().createQuery("delete from RaceCategory").executeUpdate();
		getEntityManager().createQuery("delete from Category").executeUpdate();
		getEntityManager().createQuery("delete from Course").executeUpdate();
		getEntityManager().createQuery("delete from Period").executeUpdate();
		getEntityManager().createQuery("delete from Race").executeUpdate();
		getEntityManager().createQuery("delete from Health").executeUpdate();
		getEntityManager().createQuery("delete from Profile").executeUpdate();
		getEntityManager().createQuery("delete from User").executeUpdate();
	}

	@POST
	@Transactional
	@Path("reload")
	public void reload(@Context UriInfo uriInfo) throws Exception {
		validate(uriInfo);
		unload(uriInfo);
		load(uriInfo);
	}

	@POST
	@Path("load")
	@Transactional
	public void load(@Context UriInfo uriInfo) throws Exception {
		validate(uriInfo);

		User[] users = new User[50];
		for (int i = 0; i < users.length; i++) {
			String email = "guest_" + i + "@guest.com";
			String password = "guest";
			String name;
			GenderType gender;
			boolean verified = true;

			if (i % 3 == 0) {
				name = "Usuário homem de exemplo #" + i;
				gender = MALE;
			} else {
				name = "Usuário mulher de exemplo #" + i;
				gender = FEMALE;
			}

			if (i % 2 == 1) {
				verified = false;
			}

			users[i] = newUser(email, password, name, gender, verified);
		}

		// newUser("cleverson.sacramento@gmail.com", "123", "Cleverson Sacramento", MALE, true);
		// newUser("cleverson.sacramento@gmail.com", "123", "Cleverson Sacramento", MALE, false);
		// newUser("cleverson.sacramento@gmail.com", null, "Cleverson Sacramento", MALE, true);
		newUser("lucianosantosborges@gmail.com", "123", "Luciano Borges", MALE, true);

		User arnaldoMaciel = newUser("arnaldo_maciel@hotmail.com", "123", "Arnaldo Maciel", MALE, true);
		User gustavoChagas = newUser("chagas77@yahoo.com.br", "123", "Gustavo Chagas", MALE, true);

		newAnnualFee(2015, 10);

		Category quarteto = newCategory("Quarteto", "Quarteto contendo pelo menos uma mulher", 4, 1, 1);
		Category duplaMasc = newCategory("Dupla masculina", "Dupla composta apenas por homens", 2, 2, null);
		Category duplaFem = newCategory("Dupla feminina", "Dupla composta apenas por mulheres", 2, null, 2);
		Category duplaMista = newCategory("Dupla mista", "Dupla composta por um homem e uma mulher", 2, 1, 1);
		Category trioMasc = newCategory("Trio masculino", "Trio composto apenas por homens", 3, 3, null);
		Category trioMisto = newCategory("Trio misto", "Trio contendo pelo menos uma mulher", 3, 1, 1);
		Category solo = newCategory("Solo", "Único integrante independente do sexo", 1, null, null);

		String description = "3ª Noite do Perrengue – Corrida de aventura com MTB, Trekking, Remo e Modalidade Surpresa. O melhor local, a melhor estrutura, a melhor diversão. Válido pelo RBCA – Ranking Brasileiro de Corrida de Aventura.";
		Race np = newRace("Noite do Perrengue 3", description, "21/03/2015");
		np.setBanner(Strings.parse(Reflections.getResourceAsStream("temp/np3-banner-base64.txt")).getBytes());
		np.setCity(getEntityManager().find(City.class, 5571));
		newPeriod(np, "01/01/2015", "31/01/2015", 799.00);
		newPeriod(np, "01/02/2015", "20/02/2015", 80.00);
		newPeriod(np, "21/02/2015", "10/03/2015", 90.00);
		newPeriod(np, "11/03/2015", "20/03/2015", 100.00);

		Course np45km = newCourse(np, "45 Km");
		// Course np100km = newCourse(np, 100);
		newRaceCategory(np, np45km, duplaMasc);
		newRaceCategory(np, np45km, duplaFem);
		newRaceCategory(np, np45km, duplaMista);
		// newRaceCategory(np, np100km, duplaMasc);
		// newRaceCategory(np, np100km, duplaMista);
		// RaceCategory npQuarteto100km = newRaceCategory(np, np100km, quarteto);

		newRaceOrganizer(np, arnaldoMaciel);
		newRaceOrganizer(np, gustavoChagas);

		newRace("CARI - Sol do Salitre", null, "11/04/2015");
		newRace("CICA - Mandacaru", null, "18/04/2015");
		newRace("CICA - Peleja", null, "13/06/2015");
		newRace("CARI - Casco de Peba", null, "20/06/2015");

		Race ct = newRace("Corrida do CT Gantuá", null, "07/07/2015");
		ct.setCity(getEntityManager().find(City.class, 544));

		newRace("CARI - Laskpé", null, "15/08/2015");
		newRace("CICA - Cangaço", null, "30/08/2015");
		newRace("CARI - Desafio dos Sertões", null, "10/10/2015");
		newRace("CARI - Integração", null, "05/12/2015");

		// newRegistration("Quarteto Exemplo", npQuarteto100km, new User[] { users[0], users[2], users[6],
		// users[12] });
	}

	private void validate(UriInfo uriInfo) throws Exception {
		if (uriInfo.getBaseUri().toString().contains("com.br")) {
			throw new ForbiddenException().addViolation("Em produção não pode!");
		}
	}

	private AnnualFee newAnnualFee(Integer year, float fee) throws Exception {
		AnnualFee annualFee = new AnnualFee();
		annualFee.setYear(year);
		annualFee.setFee(BigDecimal.valueOf(fee));
		getEntityManager().persist(annualFee);
		return annualFee;
	}

	private TeamFormation newTeamFormation(Registration registration, User user, BigDecimal racePrice,
			BigDecimal annualFee) throws Exception {
		TeamFormation teamFormation = new TeamFormation();
		teamFormation.setRegistration(registration);
		teamFormation.setUser(user);
		teamFormation.setRacePrice(racePrice);
		teamFormation.setAnnualFee(annualFee);

		getEntityManager().persist(teamFormation);
		return teamFormation;
	}

	// private Registration newRegistration(String teamName, RaceCategory raceCategory, User[] members) throws Exception
	// {
	// Registration registration = new Registration();
	// registration.setTeamName(teamName);
	// registration.setRaceCategory(raceCategory);
	// getEntityManager().persist(registration);
	//
	// for (int i = 0; i < members.length; i++) {
	// newTeamFormation(registration, members[i]);
	// }
	//
	// return registration;
	// }

	private Race newRace(String name, String description, String date) throws Exception {
		Race race = new Race();
		race.setName(name);
		race.setDescription(description);
		race.setDate(Dates.parse(date));
		getEntityManager().persist(race);
		return race;
	}

	private Category newCategory(String name, String description, Integer teamSize, Integer minMaleMembers,
			Integer minFemaleMembers) {
		Category category = new Category();
		category.setName(name);
		category.setDescription(description);
		category.setTeamSize(teamSize);
		category.setMinMaleMembers(minMaleMembers);
		category.setMinFemaleMembers(minFemaleMembers);
		getEntityManager().persist(category);
		return category;
	}

	private Period newPeriod(Race race, String begining, String end, Double price) throws Exception {
		Period period = new Period(race);
		period.setBeginning(Dates.parse(begining));
		period.setEnd(Dates.parse(end));
		period.setPrice(BigDecimal.valueOf(price));
		getEntityManager().persist(period);
		return period;
	}

	private Course newCourse(Race race, String name) {
		Course course = new Course(race);
		course.setName(name);
		getEntityManager().persist(course);
		return course;
	}

	private RaceCategory newRaceCategory(Race race, Course course, Category category) {
		RaceCategory availableCategory = new RaceCategory(race, course, category);
		getEntityManager().persist(availableCategory);
		return availableCategory;
	}

	private RaceOrganizer newRaceOrganizer(Race race, User organizer) {
		RaceOrganizer raceOrganizer = new RaceOrganizer(race, organizer);
		getEntityManager().persist(raceOrganizer);
		return raceOrganizer;
	}

	private User newUser(String username, String password, String name, GenderType gender, boolean verified) {
		User user = new User();
		user.setEmail(username);
		user.setPassword(Passwords.hash(password, username));
		user.setCreation(new Date());
		user.setActivation(verified ? new Date() : null);
		getEntityManager().persist(user);

		Profile profile = new Profile(user);
		profile.setName(name);
		profile.setGender(gender);
		profile.setPendencies(PendencyCounter.count(profile));
		getEntityManager().persist(profile);

		Health health = new Health(user);
		health.setPendencies(PendencyCounter.count(health));
		getEntityManager().persist(health);

		return user;
	}

	private EntityManager getEntityManager() {
		return Beans.getReference(EntityManager.class);
	}
}
