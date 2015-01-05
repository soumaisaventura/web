package br.com.fbca.persistence;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.fbca.entity.Race;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class Load {

	@Inject
	private EntityManager em;

	@Startup
	public void race() throws Exception {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyyy");
		Race race;

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

		race = new Race();
		race.setName("CICA - Mandacaru");
		race.setDate(format.parse("25/04/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CICA - Peleja");
		race.setDate(format.parse("13/06/2015"));
		em.persist(race);

		race = new Race();
		race.setName("Corrida do CT");
		race.setDate(format.parse("07/07/2015"));
		em.persist(race);

		race = new Race();
		race.setName("CICA - Cangaço");
		race.setDate(format.parse("30/08/2015"));
		em.persist(race);
	}
}
