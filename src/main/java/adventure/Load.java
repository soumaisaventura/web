package adventure;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import adventure.entity.AvailableCategory;
import adventure.entity.Category;
import adventure.entity.Course;
import adventure.entity.Period;
import adventure.entity.Race;
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

	private Category newCategory(String name, String description, Integer members) {
		Category category = new Category();
		category.setName(name);
		category.setDescription(description);
		category.setMembers(members);
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

	@SuppressWarnings("unused")
	@Startup
	public void race() throws Exception {
		em.createQuery("delete from Category").executeUpdate();
		em.createQuery("delete from Course").executeUpdate();
		em.createQuery("delete from Period").executeUpdate();
		em.createQuery("delete from Race").executeUpdate();

		Category quarteto = newCategory("Quarteto", "Quarteto contendo pelo menos uma mulher [RBCA]", 4);
		Category duplaMasculina = newCategory("Dupla masculina", "Dupla composta apenas por homens [RBCA]", 2);
		Category duplaMista = newCategory("Dupla mista", "Dupla composta por um homem e uma mulher [RBCA]", 2);
		Category trioMasculino = newCategory("Trio masculino", "Trio composto apenas por homens [RBCA]", 3);
		Category trioMisto = newCategory("Trio misto", "Trio contendo pelo menos uma mulher [RBCA]", 3);
		Category solo = newCategory("Solo", "Único integrante independente do sexo [RBCA]", 1);

		newRace("Desafio dos Sertões", "16/08/2014");
		newRace("Laskpé", "08/11/2014");

		Race np = newRace("Noite do Perrengue 3", "21/03/2015");
		newPeriod(np, "07/01/2015", "15/01/2015", 60.00);
		newPeriod(np, "16/01/2015", "31/01/2015", 80.00);
		Course course50 = newCourse(np, 50);
		Course course100 = newCourse(np, 100);
		newAvailableCategory(np, course50, duplaMasculina);
		newAvailableCategory(np, course50, duplaMista);
		newAvailableCategory(np, course100, duplaMasculina);
		newAvailableCategory(np, course100, duplaMista);
		newAvailableCategory(np, course100, quarteto);
		newAvailableCategory(np, course100, solo);

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
