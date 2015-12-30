package adventure.helper;

import java.util.Calendar;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import adventure.entity.Registration;
import adventure.persistence.RegistrationDAO;

@Named
@RequestScoped
public class RegistrationHelper {

	private boolean initialized = false;

	private String title;

	private String slug;

	private Integer year;

	public void initialize(HttpServletRequest request) {
		if (!initialized) {
			initialized = true;

			String param = request.getParameter("inscricao_id");
			Registration registration = RegistrationDAO.getInstance().loadForMeta(Long.parseLong(param));

			if (registration != null) {
				this.title = registration.getRaceCategory().getRace().getEvent().getName();
				slug = registration.getRaceCategory().getRace().getEvent().getSlug();

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(registration.getRaceCategory().getRace().getEvent().getBeginning());
				this.year = calendar.get(Calendar.YEAR);
			}
		}
	}

	public String getTitle(HttpServletRequest request) {
		initialize(request);
		return title;
	}

	public String getSlug(HttpServletRequest request) {
		initialize(request);
		return slug;

	}

	public Integer getYear(HttpServletRequest request) {
		initialize(request);
		return year;
	}
}
