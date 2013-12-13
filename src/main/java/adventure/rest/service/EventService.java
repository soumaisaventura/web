package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.Evento;
import adventure.persistence.EventoDAO;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ValidateRequest
@Path("/api/evento")
@Produces(APPLICATION_JSON)
public class EventService {

	@Inject
	private EventoDAO dao;

	@GET
	public List<Evento> search() {
		return dao.findAll();
	}

	@Startup
	@Transactional
	public void cargarTemporariaInicial() throws MalformedURLException {
		if (dao.findAll().isEmpty()) {
			Evento evento;

			evento = new Evento();
			evento.setNome("Desafio dos Sertões 2014");
			evento.setData(new Date());
			evento.setLocal("Juazeiro-BA");
			evento.setLink(new URL("http://www.desafiodossertoes.com.br"));
			dao.insert(evento);

			evento = new Evento();
			evento.setNome("Noite do Perrenge II");
			evento.setData(new Date());
			evento.setLocal("Sauípe-BA");
			evento.setLink(new URL("http://www.noitedoperrengue.com.br"));
			dao.insert(evento);
		}
	}
}
