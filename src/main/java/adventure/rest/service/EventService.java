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

import adventure.entity.Event;
import adventure.persistence.EventDAO;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ValidateRequest
@Path("/api/event")
@Produces(APPLICATION_JSON)
public class EventService {

	@Inject
	private EventDAO dao;

	@GET
	public List<Event> search() {
		return dao.findAll();
	}

	@Startup
	@Transactional
	public void loadTempData() throws MalformedURLException {
		if (dao.findAll().isEmpty()) {
			Event event;

			event = new Event();
			event.setNome("Desafio dos Sertões 2014");
			event.setData(new Date());
			event.setLocal("Juazeiro-BA");
			event.setLink(new URL("http://www.desafiodossertoes.com.br"));
			dao.insert(event);

			event = new Event();
			event.setNome("Noite do Perrenge II");
			event.setData(new Date());
			event.setLocal("Sauípe-BA");
			event.setLink(new URL("http://www.noitedoperrengue.com.br"));
			dao.insert(event);
		}
	}
}
