package adventure.helper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import adventure.entity.Race;
import adventure.persistence.RaceDAO;

@Named
@RequestScoped
public class RaceHelper {

	private boolean initialized = false;

	private String title;

	private String description;

	public void initialize(HttpServletRequest request) {
		if (!initialized) {
			initialized = true;

			String eventId = request.getParameter("evento_id");
			String raceId = request.getParameter("prova_id");
			Race race = RaceDAO.getInstance().loadMetaOgg(raceId, eventId);

			this.title = race.getEvent().getName() + " – " + race.getName();
			this.description = race.getEvent().getDescription();
		}
	}

	public String getTitle(HttpServletRequest request) {
		initialize(request);
		return title;
	}

	public String getDescription(HttpServletRequest request) {
		initialize(request);
		return description;
	}
}
