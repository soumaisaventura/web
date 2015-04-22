//package adventure.helper;
//
//import java.io.Serializable;
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Locale;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Named;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import adventure.entity.Race;
//import adventure.entity.Registration;
//import adventure.entity.TeamFormation;
//import adventure.persistence.RegistrationDAO;
//import adventure.persistence.TeamFormationDAO;
//import adventure.util.Misc;
//
//@Named
//@RequestScoped
//public class RegistrationPublicHelper implements Serializable {
//
//	private static final long serialVersionUID = 1L;
//
//	private String title;
//
//	private String siteName;
//
//	private String url;
//
//	private String description;
//
//	private String image;
//
//	private String raceUrl;
//
//	public void initialize(HttpServletRequest request, HttpServletResponse response) {
//		String registrationId = request.getParameter("registration");
//		Long id = Long.parseLong(registrationId);
//
//		// HttpServletREs
//
//		Registration registration = RegistrationDAO.getInstance().loadForDetails(id);
//		Race race = registration.getRaceCategory().getRace();
//
//		SimpleDateFormat formatter = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
//
//		Pattern pattern = Pattern.compile("(.+)/registration\\-public\\.jsf");
//		Matcher matcher = pattern.matcher(request.getRequestURL().toString());
//		matcher.matches();
//		String baseUrl = matcher.group(1);
//
//		List<TeamFormation> members = TeamFormationDAO.getInstance().find(registration);
//
//		this.title = registration.getTeamName() + " vai para a " + race.getName() + "!";
//		this.url = baseUrl + "/registration/" + registrationId + "/public";
//		this.description = "A equipe " + registration.getTeamName() + ", composta por "
//				+ Misc.stringfyTeamFormation(members) + ", se inscreveu na prova " + race.getName()
//				+ " que acontecerá no dia " + formatter.format(race.getDate()).toLowerCase() + " em "
//				+ race.getCity().getName() + "/" + race.getCity().getState().getAbbreviation() + ".";
//		this.image = baseUrl + "/api/race/" + race.getId() + "/banner";
//		this.raceUrl = baseUrl + "/race/" + race.getId();
//	}
//
//	public String getTitle() {
//		return title;
//	}
//
//	public String getSiteName() {
//		return siteName;
//	}
//
//	public String getUrl() {
//		return url;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public String getImage() {
//		return image;
//	}
//
//	public String getRaceUrl() {
//		return raceUrl;
//	}
//}
