package adventure;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import adventure.entity.Period;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class Load {

	@Inject
	private EntityManager em;

	@Startup
	public void race() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyyy");

		em.createQuery("delete from Period").executeUpdate();
		em.createQuery("delete from Race").executeUpdate();

		Race race;
		Period period;

		race = new Race();
		race.setName("Desafio dos Sertões");
		race.setDate(format.parse("16/08/2014"));
		em.persist(race);

		race = new Race();
		race.setName("Laskpé");
		race.setDate(format.parse("08/11/2014"));
		em.persist(race);

		race = new Race();
		race.setName("Noite do Perrengue 3");
		race.setDate(format.parse("21/03/2015"));
		em.persist(race);
		period = new Period(race);
		period.setBeginning(format.parse("07/01/2015"));
		period.setEnd(format.parse("15/01/2015"));
		period.setPrice(BigDecimal.valueOf(60.00));
		em.persist(period);
		period = new Period(race);
		period.setBeginning(format.parse("16/01/2015"));
		period.setEnd(format.parse("31/01/2015"));
		period.setPrice(BigDecimal.valueOf(80.00));
		em.persist(period);

		race = new Race();
		race.setName("CARI - Sol do Salitre");
		race.setDate(format.parse("18/04/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CICA - Mandacaru");
		race.setDate(format.parse("25/04/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CICA - Peleja");
		race.setDate(format.parse("13/06/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CARI - Peleja");
		race.setDate(format.parse("20/06/2015"));
		em.persist(race);

		race = new Race();
		race.setName("Corrida do CT");
		race.setDate(format.parse("07/07/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CARI - Laskpé");
		race.setDate(format.parse("15/08/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CICA - Cangaço");
		race.setDate(format.parse("30/08/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CARI - Desafio dos Sertões");
		race.setDate(format.parse("10/10/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CARI - Integração");
		race.setDate(format.parse("05/12/2015"));
		em.persist(race);
	}
}
