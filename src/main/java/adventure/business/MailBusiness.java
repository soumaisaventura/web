package adventure.business;

import static javax.mail.Message.RecipientType.TO;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import adventure.entity.Race;
import adventure.entity.RaceCategory;
import adventure.entity.Registration;
import adventure.entity.UserRegistration;
import adventure.entity.User;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.UserRegistrationDAO;
import adventure.persistence.UserDAO;
import adventure.security.Passwords;
import adventure.util.ApplicationConfig;
import adventure.util.Dates;
import adventure.util.Misc;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;

@Stateless
public class MailBusiness implements Serializable {

	private static final long serialVersionUID = 1L;

	public static MailBusiness getInstance() {
		return Beans.getReference(MailBusiness.class);
	}

	@Asynchronous
	public void sendUserActivation(final String email, final URI baseUri) throws Exception {
		UserDAO userDAO = UserDAO.getInstance();
		User user = userDAO.loadForAuthentication(email);
		final String token;

		if (user.getActivationToken() == null) {
			token = Passwords.randomToken();
			User persisted = userDAO.load(user.getId());
			persisted.setActivationToken(Passwords.hash(token, persisted.getEmail()));
			userDAO.update(persisted);
		} else {
			token = user.getActivationToken();
		}

		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/activation.html"));
		content = clearContent(content);
		content = content.replace("{name}", escapeHtml(user.getProfile().getName()));
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
				"$1" + baseUri.resolve("atleta/ativacao?token=" + token).toString() + "$2");
		send("Confirmação de e-mail", content, "text/html", email);
	}

	@Asynchronous
	public void sendWelcome(User user, URI baseUri) throws Exception {
		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/welcome.html"));
		content = clearContent(content);
		content = content.replace("{name}", escapeHtml(user.getProfile().getName()));
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">url1.url1)", "$1" + baseUri.toString() + "$2");
		content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\")", "$1"
				+ baseUri.resolve("atleta/pessoal").toString() + "$2");
		content = content.replace("url1.url1",
				baseUri.toString().endsWith("/") ? baseUri.toString().substring(0, baseUri.toString().length() - 1)
						: baseUri.toString());
		send("Seja bem-vindo!", content, "text/html", user.getEmail());
	}

	@Asynchronous
	public void sendPasswordCreationMail(final String email, final URI baseUri) throws Exception {
		UserDAO userDAO = UserDAO.getInstance();
		User user = userDAO.loadForAuthentication(email);
		final String token;

		if (user.getPasswordResetToken() == null) {
			token = Passwords.randomToken();
			User persisted = userDAO.load(user.getId());
			persisted.setPasswordResetToken(Passwords.hash(token, persisted.getEmail()));
			persisted.setPasswordResetRequest(new Date());
			userDAO.update(persisted);
		} else {
			token = user.getPasswordResetToken();
		}

		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/password-creation.html"));
		content = clearContent(content);
		content = content.replace("{name}", escapeHtml(user.getProfile().getName()));
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
				"$1" + baseUri.resolve("senha/redefinicao?token=" + token).toString() + "$2");
		send("Criação de senha", content, "text/html", email);
	}

	@Asynchronous
	public void sendResetPasswordMail(final String email, final URI baseUri) throws Exception {
		UserDAO userDAO = UserDAO.getInstance();
		User user = userDAO.loadForAuthentication(email);
		final String token;

		if (user.getPasswordResetToken() == null) {
			token = Passwords.randomToken();
			User persisted = userDAO.load(user.getId());
			persisted.setPasswordResetToken(Passwords.hash(token, persisted.getEmail()));
			persisted.setPasswordResetRequest(new Date());
			userDAO.update(persisted);
		} else {
			token = user.getPasswordResetToken();
		}

		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/password-recovery.html"));
		content = clearContent(content);
		content = content.replace("{name}", escapeHtml(user.getProfile().getName()));
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
				"$1" + baseUri.resolve("senha/redefinicao?token=" + token).toString() + "$2");
		send("Recuperação de senha", content, "text/html", email);
	}

	@Asynchronous
	public void sendAccountRemoval(User user, String dupEmail) throws Exception {
		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/account-removal.html"));
		content = clearContent(content);
		content = content.replace("{name}", escapeHtml(user.getProfile().getName()));
		content = content.replace("{email}", dupEmail);
		content = content.replace("{originalEmail}", user.getEmail());
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		send("Remoção de conta", content, "text/html", dupEmail);
	}

	@Asynchronous
	public void sendRegistrationCreation(Registration registration, URI baseUri) throws Exception {
		List<UserRegistration> members = UserRegistrationDAO.getInstance().find(registration);
		registration = RegistrationDAO.getInstance().loadForDetails(registration.getId());
		Race race = registration.getRaceCategory().getRace();

		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/registration-creation.html"));
		content = clearContent(content);
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replace("{registrationTeamName}", escapeHtml(registration.getTeamName()));
		content = content.replace("{raceName}", escapeHtml(race.getName()));
		// content = content.replace("{raceCity}", escapeHtml(race.getCity().getName()));
		// content = content.replace("{raceState}", race.getCity().getState().getAbbreviation());
		// content = content.replace("{raceDate}", Dates.parse(race.getDate()));
		// content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
		// "$1" + baseUri.resolve("inscricao/" + registration.getFormattedId()).toString() + "$2");
		content = content.replace("{registrationId}", registration.getFormattedId());
		content = content.replace("{registrationDate}", Dates.parse(registration.getDate()));
		content = content.replace("{categoryName}", escapeHtml(registration.getRaceCategory().getCategory().getName()));
		// content = content.replace("{courseName}", registration.getRaceCategory().getCourse().getName());
		content = content.replace("{teamFormation}", escapeHtml(Misc.stringfyTeamFormation(members)));

		// String replacement = "";
		// for (User organizer : UserDAO.getInstance().findRaceOrganizers(race)) {
		// replacement += "\n$1" + organizer.getProfile().getName() + "; tel: " + organizer.getProfile().getMobile()
		// + "; " + organizer.getEmail() + "$2\r";
		// }
		// content = content.replaceAll("(<ul.+)\\{organizerInfo\\}(.+ul>)", replacement);

		String subject = "Pedido de inscrição";
		subject += " #" + registration.getFormattedId();
		subject += " – " + race.getName();

		for (UserRegistration member : members) {
			send(subject, content, "text/html", member.getUser().getEmail());
		}
	}

	@Asynchronous
	public void sendRegistrationFailed(User member, User submitter, RaceCategory raceCategory, String teamName,
			URI baseUri) throws Exception {
		Race race = raceCategory.getRace();
		String pendency = (member.getProfile().getPendencies() > 0 ? "pessoal" : "saude");

		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/registration-failed.html"));
		content = clearContent(content);
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replace("{name}", escapeHtml(member.getProfile().getName()));
		content = content.replace("{registrationSubmitter}",
				escapeHtml(member.getId().equals(submitter.getId()) ? "você" : submitter.getProfile().getName()));
		content = content.replace("{registrationTeamName}", escapeHtml(teamName));
		content = content.replace("{what}", member.getId().equals(submitter.getId()) ? "se inscrever"
				: "inscrever você");
		content = content.replace("{raceName}", escapeHtml(race.getName()));
		// content = content.replace("{raceDate}", Dates.parse(race.getDate()));
		content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
				"$1" + baseUri.resolve("atleta/" + pendency + "$2"));

		String subject = "Tentativa de inscrição";
		subject += " – " + race.getName();

		send(subject, content, "text/html", member.getEmail());
	}

	@Asynchronous
	public void sendRegistrationPeriodChanging(Registration registration, Date newPeriodBegining, Date newPeriodEnd,
			URI baseUri) throws Exception {
		List<UserRegistration> members = UserRegistrationDAO.getInstance().find(registration);
		registration = RegistrationDAO.getInstance().loadForDetails(registration.getId());
		Race race = registration.getRaceCategory().getRace();

		String content = Strings.parse(Reflections
				.getResourceAsStream("mail-templates/registration-period-changing.html"));
		content = clearContent(content);
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replace("{raceName}", escapeHtml(race.getName()));
		content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
				"$1" + baseUri.resolve("inscricao/" + registration.getFormattedId()).toString() + "$2");
		content = content.replace("{registrationId}", registration.getFormattedId());
		content = content.replace("{teamFormation}", escapeHtml(Misc.stringfyTeamFormation(members)));
		content = content.replace("{newPeriodBegining}", Dates.parse(newPeriodBegining));
		content = content.replace("{newPeriodEnd}", Dates.parse(newPeriodEnd));
		// content = content.replaceAll("(<ul.+)\\{organizerInfo\\}(.+ul>)", escapeHtml(getOrganizerInfo(race)));

		String subject = "Reajuste no pedido";
		subject += " #" + registration.getFormattedId();
		subject += " – " + race.getName();

		for (UserRegistration member : members) {
			send(subject, content, "text/html", member.getUser().getEmail());
		}
	}

	@Asynchronous
	public void sendRegistrationCancellation(Registration registration, URI baseUri) throws Exception {
		List<UserRegistration> members = UserRegistrationDAO.getInstance().find(registration);
		registration = RegistrationDAO.getInstance().loadForDetails(registration.getId());
		Race race = registration.getRaceCategory().getRace();

		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/registration-cancelled.html"));
		content = clearContent(content);
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replace("{raceName}", escapeHtml(race.getName()));
		// content = content.replace("{raceDate}", Dates.parse(race.getDate()));
		// content = content.replace("{raceCity}", escapeHtml(race.getCity().getName()));
		// content = content.replace("{raceState}", race.getCity().getState().getAbbreviation());
		// content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
		// "$1" + baseUri.resolve("inscricao/" + registration.getFormattedId()).toString() + "$2");
		content = content.replace("{registrationId}", registration.getFormattedId());
		content = content.replace("{teamFormation}", escapeHtml(Misc.stringfyTeamFormation(members)));
		// content = content.replaceAll("(<ul.+)\\{organizerInfo\\}(.+ul>)", escapeHtml(getOrganizerInfo(race)));

		String subject = "Cancelamento da inscrição";
		subject += " #" + registration.getFormattedId();
		subject += " – " + race.getName();

		for (UserRegistration member : members) {
			send(subject, content, "text/html", member.getUser().getEmail());
		}
	}

	// private String getOrganizerInfo(Race race) {
	// String replacement = "";
	// for (User organizer : UserDAO.getInstance().findRaceOrganizers(race)) {
	// replacement += "\n$1" + organizer.getProfile().getName() + "; tel: " + organizer.getProfile().getMobile()
	// + "; " + organizer.getEmail() + "$2\r";
	// }
	//
	// return replacement;
	// }

	@Asynchronous
	public void sendRegistrationConfirmation(Registration registration, URI baseUri) throws Exception {
		// User creator = userDAO.loadBasics(registration.getSubmitter().getEmail());
		List<UserRegistration> members = UserRegistrationDAO.getInstance().find(registration);
		registration = RegistrationDAO.getInstance().loadForDetails(registration.getId());
		Race race = registration.getRaceCategory().getRace();

		String content = Strings
				.parse(Reflections.getResourceAsStream("mail-templates/registration-confirmation.html"));
		content = clearContent(content);
		content = content.replace("{appName}", "Sou+ Aventura");
		content = content.replace("{appAdminMail}", "contato@soumaisaventura.com.br");
		content = content.replace("{registrationTeamName}", escapeHtml(registration.getTeamName()));
		content = content.replace("{raceName}", escapeHtml(race.getName()));
		// content = content.replace("{raceCity}", escapeHtml(race.getCity().getName()));
		// content = content.replace("{raceState}", race.getCity().getState().getAbbreviation());
		// content = content.replace("{raceDate}", Dates.parse(race.getDate()));
		// content = content.replaceAll("(href=\")https?://[\\w\\./-]+/(\">)",
		// "$1" + baseUri.resolve("inscricao/" + registration.getFormattedId()).toString() + "$2");
		content = content.replace("{registrationId}", registration.getFormattedId());
		content = content.replace("{categoryName}", escapeHtml(registration.getRaceCategory().getCategory().getName()));
		// content = content.replace("{courseName}", registration.getRaceCategory().getCourse().getName());
		content = content.replace("{teamFormation}", escapeHtml(Misc.stringfyTeamFormation(members)));

		String subject = "Confirmação da inscrição";
		subject += " #" + registration.getFormattedId();
		subject += " – " + race.getName();

		for (UserRegistration member : members) {
			send(subject, content, "text/html", member.getUser().getEmail());
		}

		// if (!members.contains(creator)) {
		// send(subject, content, "text/html", creator.getEmail());
		// }
	}

	private String clearContent(String content) {
		return content.replaceAll("<div id=\"campaign\">.+</div>", "");
	}

	private void send(final String subject, final String content, final String type, final String to) throws Exception {
		// new Thread() {
		//
		// public void run() {
		// try {
		ApplicationConfig config = getConfig();
		final String PREFIX = config.getAppTitle() + " – ";
		final String SUFIX = "";

		MimeMessage message = new MimeMessage(getSession());
		message.setFrom(new InternetAddress("contato@soumaisaventura.com.br"));
		message.setSubject(PREFIX + subject + SUFIX, "UTF-8");
		// message.setRecipients(TO, Strings.join(",", to));
		message.setRecipients(TO, to);
		message.setContent(content, type);

		Transport.send(message);

		// } catch (MessagingException cause) {
		// throw new RuntimeException(cause);
		// }
		// };
		//
		// }.start();
	}

	private Session getSession() {
		final ApplicationConfig config = getConfig();
		Properties props = new Properties();
		props.put("mail.smtp.host", config.getMailSmtpHost());
		props.put("mail.smtp.auth", "true");

		if (config.getMailSmtpPort() != null) {
			props.put("mail.smtp.port", config.getMailSmtpPort());
			props.put("mail.smtp.socketFactory.port", config.getMailSmtpPort());
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}

		if (config.getMailSmtpTls() != null) {
			props.put("mail.smtp.starttls.enable", config.getMailSmtpTls());
		}

		Authenticator authenticator = new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(config.getMailSmtpUser(), config.getMailSmtpPassword());
			}
		};

		return Session.getInstance(props, authenticator);
	}

	private ApplicationConfig getConfig() {
		return Beans.getReference(ApplicationConfig.class);
	}
}
