package adventure;

import static adventure.entity.Gender.FEMALE;
import static adventure.entity.Gender.MALE;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import adventure.entity.Category;
import adventure.entity.City;
import adventure.entity.Course;
import adventure.entity.Gender;
import adventure.entity.Health;
import adventure.entity.Period;
import adventure.entity.Profile;
import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.entity.RaceOrganizer;
import adventure.entity.Registration;
import adventure.entity.TeamFormation;
import adventure.entity.User;
import adventure.security.Passwords;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;

@Transactional
public class Load {

	@Inject
	private EntityManager em;

	private TeamFormation newTeamFormation(Registration registration, User user, boolean confirmed) throws Exception {
		TeamFormation teamFormation = new TeamFormation(registration, user);
		teamFormation.setConfirmed(confirmed);
		em.persist(teamFormation);
		return teamFormation;
	}

	@SuppressWarnings("unused")
	private Registration newRegistration(String teamName, RaceCategory raceCategory, User[] members) throws Exception {
		Registration registration = new Registration();
		registration.setTeamName(teamName);
		registration.setRaceCategory(raceCategory);
		em.persist(registration);

		for (int i = 0; i < members.length; i++) {
			newTeamFormation(registration, members[i], i % 2 == 0);
		}

		return registration;
	}

	private Race newRace(String name, String description, String date) throws Exception {
		Race race = new Race();
		race.setName(name);
		race.setDescription(description);
		race.setDate(Dates.parse(date));
		em.persist(race);
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
		em.persist(category);
		return category;
	}

	private Period newPeriod(Race race, String begining, String end, Double price) throws Exception {
		Period period = new Period(race);
		period.setBeginning(Dates.parse(begining));
		period.setEnd(Dates.parse(end));
		period.setPrice(BigDecimal.valueOf(price));
		em.persist(period);
		return period;
	}

	private Course newCourse(Race race, Integer length) {
		Course course = new Course(race);
		course.setLength(length);
		em.persist(course);
		return course;
	}

	private RaceCategory newRaceCategory(Race race, Course course, Category category) {
		RaceCategory availableCategory = new RaceCategory(race, course, category);
		em.persist(availableCategory);
		return availableCategory;
	}

	private RaceOrganizer newRaceOrganizer(Race race, User organizer) {
		RaceOrganizer raceOrganizer = new RaceOrganizer(race, organizer);
		em.persist(raceOrganizer);
		return raceOrganizer;
	}

	private User newUser(String username, String password, String name, Gender gender, boolean verified) {
		User user = new User();
		user.setEmail(username);
		user.setPassword(Passwords.hash(password, username));
		user.setCreation(new Date());
		user.setActivation(verified ? new Date() : null);
		em.persist(user);

		Profile profile = new Profile(user);
		profile.setName(name);
		profile.setGender(gender);
		em.persist(profile);

		Health health = new Health(user);
		em.persist(health);

		return user;
	}

//	@Startup
	@SuppressWarnings("unused")
	public void perform() throws Exception {
		em.createQuery("delete from Registration").executeUpdate();
		em.createQuery("delete from TeamFormation").executeUpdate();
		em.createQuery("delete from RaceCategory").executeUpdate();
		em.createQuery("delete from Category").executeUpdate();
		em.createQuery("delete from Course").executeUpdate();
		em.createQuery("delete from Period").executeUpdate();
		em.createQuery("delete from Race").executeUpdate();
		em.createQuery("delete from Health").executeUpdate();
		em.createQuery("delete from Profile").executeUpdate();
		em.createQuery("delete from User").executeUpdate();

		User[] users = new User[50];
		for (int i = 0; i < users.length; i++) {
			String email = "guest_" + i + "@guest.com";
			String password = "guest";
			String name;
			Gender gender;
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

		newUser("cleverson.sacramento@gmail.com", "123", "Cleverson Sacramento", MALE, true);
		// newUser("cleverson.sacramento@gmail.com", "123", "Cleverson Sacramento", MALE, false);
		// newUser("cleverson.sacramento@gmail.com", null, "Cleverson Sacramento", MALE, true);
		newUser("lucianosantosborges@gmail.com", "123", "Luciano Borges", MALE, true);

		User arnaldoMaciel = newUser("arnaldo_maciel@hotmail.com", "123", "Arnaldo Maciel", MALE, true);
		User gustavoChagas = newUser("chagas77@yahoo.com.br", "123", "Gustavo Chagas", MALE, true);

		Category quarteto = newCategory("Quarteto", "Quarteto contendo pelo menos uma mulher", 4, 1, 1);
		Category duplaMasc = newCategory("Dupla masculina", "Dupla composta apenas por homens", 2, 2, null);
		Category duplaFem = newCategory("Dupla feminina", "Dupla composta apenas por mulheres", 2, null, 2);
		Category duplaMista = newCategory("Dupla mista", "Dupla composta por um homem e uma mulher", 2, 1, 1);
		Category trioMasc = newCategory("Trio masculino", "Trio composto apenas por homens", 3, 3, null);
		Category trioMisto = newCategory("Trio misto", "Trio contendo pelo menos uma mulher", 3, 1, 1);
		Category solo = newCategory("Solo", "Único integrante independente do sexo", 1, null, null);

		// newRace("Desafio dos Sertões", null, "16/08/2014");
		// newRace("Laskpé", null, "08/11/2014");

		String description = "3ª Noite do Perrengue – Corrida de aventura com MTB, Trekking, Remo e Modalidade Surpresa. O melhor local, a melhor estrutura, a melhor diversão. Válido pelo RBCA – Ranking Brasileiro de Corrida de Aventura.";
		Race np = newRace("Noite do Perrengue 3", description, "21/03/2015");
		np.setBanner(Strings.parse(Reflections.getResourceAsStream("temp/np3-banner-base64.txt")).getBytes());
		np.setCity(em.find(City.class, Long.valueOf(5571)));
		newPeriod(np, "01/01/2015", "31/01/2015", 200.00);
		newPeriod(np, "01/02/2015", "20/02/2015", 80.00);
		newPeriod(np, "21/02/2015", "10/03/2015", 90.00);
		newPeriod(np, "11/03/2015", "20/03/2015", 100.00);

		Course np45km = newCourse(np, 45);
		Course np100km = newCourse(np, 100);
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
		ct.setCity(em.find(City.class, Long.valueOf(544)));

		newRace("CARI - Laskpé", null, "15/08/2015");
		newRace("CICA - Cangaço", null, "30/08/2015");
		newRace("CARI - Desafio dos Sertões", null, "10/10/2015");
		newRace("CARI - Integração", null, "05/12/2015");

		// newRegistration("Quarteto Exemplo", npQuarteto100km, new User[] { users[0], users[2], users[6],
		// users[12] });
	}
}
