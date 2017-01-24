package html;

import core.entity.Event;
import core.persistence.EventDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Named
@RequestScoped
public class EventHelper {

    private boolean initialized = false;

    private String title;

    private String slug;

    private String description;

    private Integer year;

    public void initialize(HttpServletRequest request) {
        if (!initialized) {
            initialized = true;

            String param = request.getParameter("evento_id");
            Event event = EventDAO.getInstance().loadForMeta(param);

            if (event != null) {
                this.title = event.getName();
                slug = event.getAlias();
                this.description = event.getDescription();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(event.getBeginning());
                this.year = calendar.get(Calendar.YEAR);
            }
        }
    }

    public String getTitle(HttpServletRequest request) {
        initialize(request);
        return title;
    }

    public String getAlias(HttpServletRequest request) {
        initialize(request);
        return slug;

    }

    public String getDescription(HttpServletRequest request) {
        initialize(request);
        return description;

    }

    public Integer getYear(HttpServletRequest request) {
        initialize(request);
        return year;
    }
}
