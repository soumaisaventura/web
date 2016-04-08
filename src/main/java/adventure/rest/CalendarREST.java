package adventure.rest;

import adventure.entity.Event;
import adventure.persistence.EventDAO;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.CompatibilityHints;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import static net.fortuna.ical4j.model.property.CalScale.GREGORIAN;
import static net.fortuna.ical4j.model.property.Method.PUBLISH;
import static net.fortuna.ical4j.model.property.Version.VERSION_2_0;
import static net.fortuna.ical4j.util.CompatibilityHints.KEY_OUTLOOK_COMPATIBILITY;

@Path("calendar")
public class CalendarREST {

	@GET
	@Path("{year : \\d+}")
	@Produces("text/calendar")
	public String getEventsByYear(@PathParam("year") Integer year, @Context UriInfo uriInfo) throws Exception {
		CompatibilityHints.setHintEnabled(KEY_OUTLOOK_COMPATIBILITY, true);

		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Sou+ Aventura//Calendário " + year + "//PT"));
		calendar.getProperties().add(VERSION_2_0);
		calendar.getProperties().add(GREGORIAN);
		calendar.getProperties().add(PUBLISH);

		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		VTimeZone tz = registry.getTimeZone("America/Bahia").getVTimeZone();
		calendar.getComponents().add(tz);

		for (Event event : EventDAO.getInstance().findByYear(year)) {
			Date start = new Date(event.getBeginning());
			Date end = new Date(new DateTime(event.getEnd()).plusDays(1).toDate());

			VEvent vEvent = new VEvent(start, end, event.getName());
			vEvent.getProperties().add(getUid(event, uriInfo));
			vEvent.getProperties().add(new Description(event.getDescription()));

			String location = event.getCity().getName() + ", " + event.getCity().getState().getName();
			vEvent.getProperties().add(new Location(location));

			calendar.getComponents().add(vEvent);
		}

		return calendar.toString();
	}

	private Uid getUid(Event event, UriInfo uriInfo) {
		String prefix = Hex.encodeHexString((event.getId() + "-" + uriInfo.getPath()).getBytes());
		String sufix = uriInfo.getRequestUri().getHost();
		return new Uid(prefix + "@" + sufix);
	}
}
