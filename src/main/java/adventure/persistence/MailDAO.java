package adventure.persistence;

import static javax.mail.Message.RecipientType.TO;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import adventure.entity.Period;
import adventure.entity.Profile;
import adventure.entity.Registration;
import adventure.entity.User;
import adventure.security.Passwords;
import adventure.util.ApplicationConfig;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;

@Transactional
public class MailDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public static MailDAO getInstance() {
		return Beans.getReference(MailDAO.class);
	}

	@Inject
	private ApplicationConfig config;

	@Inject
	private UserDAO userDAO;

	public void sendUserActivation(final String email, final URI baseUri) throws Exception {
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
		content = content.replace("{name}", user.getProfile().getName());
		content = content.replace("{url}", baseUri.resolve("activation?token=" + token).toString());

		send("Portal FBCA - Confirmação de e-mail", content, "text/html", email);
	}

	public void sendWelcome(User user, URI baseUri) throws Exception {
		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/welcome.html"));
		content = content.replace("{name}", user.getProfile().getName());
		content = content.replace("{url}", baseUri.toString());

		send("Portal FBCA - Seja bem-vindo!", content, "text/html", user.getEmail());
	}

	public void sendPasswordCreationMail(final String email, final URI baseUri) throws Exception {
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
		content = content.replace("{name}", user.getProfile().getName());
		content = content.replace("{url}", baseUri.resolve("password/reset?token=" + token).toString());
		send("Portal FBCA - Criação de senha", content, "text/html", email);
	}

	public void sendResetPasswordMail(final String email, final URI baseUri) throws Exception {
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
		content = content.replace("{name}", user.getProfile().getName());
		content = content.replace("{url}", baseUri.resolve("password/reset?token=" + token).toString());
		send("Portal FBCA - Recuperação de senha", content, "text/html", email);
	}

	public void sendRegistrationCreation(Registration registration, List<User> members, URI baseUri) throws Exception {
		User creator = userDAO.loadBasics(registration.getCreator().getEmail());
		registration = Beans.getReference(RegistrationDAO.class).loadForMail(registration.getId());

		String[] memberNames = new String[members.size()];
		for (int i = 0; i < members.size(); i++) {
			// TODO Não tem necessidade de novo select. Já poderia vir preenchido
			Profile profile = Beans.getReference(ProfileDAO.class).load(members.get(i));
			memberNames[i] = profile.getName();
		}

		String content = Strings.parse(Reflections.getResourceAsStream("mail-templates/registration-creation.html"));
		content = content.replace("{name}", creator.getProfile().getName());
		content = content.replace("{teamName}", registration.getTeamName());
		content = content.replace("{raceName}", registration.getRaceCategory().getRace().getName());
		content = content.replace("{raceDate}", Dates.parse(registration.getRaceCategory().getRace().getDate()));
		content = content.replace("{url}", baseUri.resolve("registration/" + registration.getId()).toString());
		content = content.replace("{registrationId}", registration.getFormattedId());
		content = content.replace("{registrationDate}", Dates.parse(registration.getDate()));
		content = content.replace("{categoryName}", registration.getRaceCategory().getCategory().getName());
		content = content.replace("{courseLength}", registration.getRaceCategory().getCourse().getLength().toString());
		content = content.replace("{members}", Strings.join(" / ", memberNames));

		Period period = Beans.getReference(PeriodDAO.class).loadCurrent(registration.getRaceCategory().getRace());
		content = content.replace("{racePrice}", period.getPrice().toString().replace(".", ","));

		String subject = "Portal FBCA - Pedido de inscrição";
		subject += " #" + registration.getFormattedId();
		subject += " - " + registration.getRaceCategory().getRace().getName();

		send(subject, content, "text/html", creator.getEmail());
	}

	private void send(final String subject, final String content, final String type, final String... to) {
		new Thread() {

			public void run() {
				try {
					MimeMessage message = new MimeMessage(getSession());
					message.setFrom(new InternetAddress("contato@fbca.com.br"));
					message.setSubject(subject, "UTF-8");
					message.setRecipients(TO, Strings.join(",", to));
					message.setContent(content, type);

					Transport.send(message);

				} catch (MessagingException cause) {
					throw new RuntimeException(cause);
				}
			};

		}.start();
	}

	// private User getUser(String email) {
	// User user = userDAO.loadFull(email);
	//
	// if (user == null) {
	// throw new IllegalStateException("Nenhuma conta associada ao e-mail " + email);
	// }
	//
	// return user;
	// }

	private Session getSession() {
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
}
