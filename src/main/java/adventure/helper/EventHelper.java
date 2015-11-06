package adventure.helper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import adventure.entity.Event;
import adventure.persistence.EventDAO;

@Named
@RequestScoped
public class EventHelper {

	private boolean initialized = false;

	private String title;

	private String description;

	public void initialize(HttpServletRequest request) {
		if (!initialized) {
			initialized = true;

			String param = request.getParameter("slug");
			Event event = EventDAO.getInstance().loadForMeta(param);

			if (event != null) {
				this.title = event.getName();
				this.description = event.getDescription();
			}
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
