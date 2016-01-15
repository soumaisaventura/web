package adventure.rest;

import static net.fortuna.ical4j.model.property.CalScale.GREGORIAN;
import static net.fortuna.ical4j.model.property.Version.VERSION_2_0;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.ProdId;
import adventure.entity.Event;
import adventure.persistence.EventDAO;

@Path("calendar")
public class CalendarREST {

	@GET
	@Path("{year : \\d+}")
	@Produces("text/calendar")
	public String getEventsByYear(@PathParam("year") Integer year) throws Exception {
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Sou+ Aventura//Calendário " + year + "//PT"));
		calendar.getProperties().add(VERSION_2_0);
		calendar.getProperties().add(GREGORIAN);

		for (Event event : EventDAO.getInstance().findByYear(year)) {
			Date start = new Date(event.getBeginning());
			Date end = new Date(event.getEnd());
			VEvent vEvent = new VEvent(start, end, event.getName());
			calendar.getComponents().add(vEvent);
		}

		return calendar.toString();
	}
}
