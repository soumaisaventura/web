package adventure.helper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.persistence.RegistrationDAO;

@Named
@RequestScoped
public class RegistrationPublicHelper implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;

	private String siteName;

	private String url;

	private String description;

	private String image;

	private String raceUrl;

	public void initialize(HttpServletRequest request) {
		String registrationId = request.getParameter("registration");
		Long id = Long.parseLong(registrationId);

		Registration registration = RegistrationDAO.getInstance().loadForDetails(id);
		Race race = registration.getRaceCategory().getRace();

		SimpleDateFormat formatter = new SimpleDateFormat("d 'de' MMMM 'de' yyyy");

		Pattern pattern = Pattern.compile("(.+)/registration\\-public\\.jsf");
		Matcher matcher = pattern.matcher(request.getRequestURL().toString());
		matcher.matches();
		String baseUrl = matcher.group(1);

		this.title = registration.getTeamName() + " vai para a " + race.getName() + "!";
		this.siteName = "Sou+ Aventura";
		this.url = baseUrl + "/registration/" + registrationId + "/public";
		this.description = "A equipe " + registration.getTeamName() + " se inscreveu na prova " + race.getName()
				+ " que acontecerá no dia " + formatter.format(race.getDate()).toLowerCase() + " em "
				+ race.getCity().getName() + "/" + race.getCity().getState().getAbbreviation() + ".";
		this.image = baseUrl + "/api/race/" + race.getId() + "/logo";
		this.raceUrl = baseUrl + "/race/" + race.getId();
	}

	public String getTitle() {
		return title;
	}

	public String getSiteName() {
		return siteName;
	}

	public String getUrl() {
		return url;
	}

	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public String getRaceUrl() {
		return raceUrl;
	}
}
