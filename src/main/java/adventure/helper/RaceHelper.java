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

			String param = request.getParameter("id");
			Race race = RaceDAO.getInstance().load(Integer.parseInt(param));

			this.title = race.getName();
			this.description = race.getDescription();
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
