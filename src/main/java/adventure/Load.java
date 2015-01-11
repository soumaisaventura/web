package adventure;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import adventure.entity.Account;
import adventure.entity.AvailableCategory;
import adventure.entity.Category;
import adventure.entity.Course;
import adventure.entity.Gender;
import adventure.entity.Health;
import adventure.entity.Period;
import adventure.entity.Profile;
import adventure.entity.Race;
import adventure.security.Passwords;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class Load {

	private static final DateFormat format = new SimpleDateFormat("dd/MM/yyyyy");

	@Inject
	private EntityManager em;

	private Race newRace(String name, String date) throws Exception {
		Race race = new Race();
		race.setName(name);
		race.setDate(format.parse(date));
		em.persist(race);
		return race;
	}

	private Category newCategory(String name, String description, Integer members, Integer minMaleMembers,
			Integer minFemaleMembers) {
		Category category = new Category();
		category.setName(name);
		category.setDescription(description);
		category.setMembers(members);
		category.setMinMaleMembers(minMaleMembers);
		category.setMinFemaleMembers(minFemaleMembers);
		em.persist(category);
		return category;
	}

	private Period newPeriod(Race race, String begining, String end, Double price) throws Exception {
		Period period = new Period(race);
		period.setBeginning(format.parse(begining));
		period.setEnd(format.parse(end));
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

	private AvailableCategory newAvailableCategory(Race race, Course course, Category category) {
		AvailableCategory availableCategory = new AvailableCategory(race, course, category);
		em.persist(availableCategory);
		return availableCategory;
	}

	private Account newAccount(String username, String password, String name, Gender gender, boolean verified) {
		Account account = new Account();
		account.setEmail(username);
		account.setPassword(Passwords.hash(password, username));
		account.setCreation(new Date());
		account.setConfirmation(verified ? new Date() : null);
		// account.setProfile(profile);
		// account.setHealth(health);
		em.persist(account);

		Profile profile = new Profile(account);
		profile.setName(name);
		profile.setGender(gender);
		em.persist(profile);

		Health health = new Health(account);
		em.persist(health);

		return account;
	}

	@SuppressWarnings("unused")
	@Startup
	public void race() throws Exception {
		em.createQuery("delete from AvailableCategory").executeUpdate();
		em.createQuery("delete from Category").executeUpdate();
		em.createQuery("delete from Course").executeUpdate();
		em.createQuery("delete from Period").executeUpdate();
		em.createQuery("delete from Race").executeUpdate();
		em.createQuery("delete from Health").executeUpdate();
		em.createQuery("delete from Profile").executeUpdate();
		em.createQuery("delete from Account").executeUpdate();

		Account[] accounts = new Account[50];
		for (int i = 0; i < accounts.length; i++) {
			String email = "guest_" + i + "@guest.com";
			String password = "guest";
			String name;
			Gender gender;
			boolean verified = true;

			if (i % 2 == 0) {
				name = "Male Guest " + i;
				gender = Gender.MALE;
			} else {
				name = "Female Guest " + i;
				gender = Gender.FEMALE;
			}

			if (i % 3 == 1) {
				verified = false;
			}

			newAccount(email, password, name, gender, verified);
		}

		Category quarteto = newCategory("Quarteto", "Quarteto contendo pelo menos uma mulher", 4, 1, 1);
		Category duplaMasculina = newCategory("Dupla masculina", "Dupla composta apenas por homens", 2, 2, null);
		Category duplaMista = newCategory("Dupla mista", "Dupla composta por um homem e uma mulher", 2, 1, 1);
		Category trioMasculino = newCategory("Trio masculino", "Trio composto apenas por homens", 3, 3, null);
		Category trioMisto = newCategory("Trio misto", "Trio contendo pelo menos uma mulher", 3, 1, 1);
		Category solo = newCategory("Solo", "Único integrante independente do sexo", 1, null, null);

		newRace("Desafio dos Sertões", "16/08/2014");
		newRace("Laskpé", "08/11/2014");

		Race np = newRace("Noite do Perrengue 3", "21/03/2015");
		newPeriod(np, "07/01/2015", "15/01/2015", 60.00);
		newPeriod(np, "16/01/2015", "31/01/2015", 80.00);
		Course np50km = newCourse(np, 50);
		Course np100km = newCourse(np, 100);
		newAvailableCategory(np, np50km, duplaMasculina);
		newAvailableCategory(np, np50km, duplaMista);
		newAvailableCategory(np, np100km, duplaMasculina);
		newAvailableCategory(np, np100km, duplaMista);
		newAvailableCategory(np, np100km, quarteto);
		newAvailableCategory(np, np100km, solo);

		newRace("CARI - Sol do Salitre", "11/04/2015");
		newRace("CICA - Mandacaru", "18/04/2015");
		newRace("CICA - Peleja", "13/06/2015");
		newRace("CARI - Casco de Peba", "20/06/2015");
		newRace("Corrida do CT", "07/07/2015");
		newRace("CARI - Laskpé", "15/08/2015");
		newRace("CICA - Cangaço", "30/08/2015");
		newRace("CARI - Desafio dos Sertões", "10/10/2015");
		newRace("CARI - Integração", "05/12/2015");
	}
}
