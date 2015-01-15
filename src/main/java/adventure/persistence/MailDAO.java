package adventure.persistence;

import static javax.mail.Message.RecipientType.TO;

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

import adventure.entity.User;
import adventure.entity.Period;
import adventure.entity.Profile;
import adventure.entity.Register;
import adventure.security.Passwords;
import adventure.util.ApplicationConfig;
import adventure.util.Dates;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Reflections;
import br.gov.frameworkdemoiselle.util.Strings;

@Transactional
public class MailDAO {

	@Inject
	private ApplicationConfig config;

	@Inject
	private UserDAO userDAO;

	public void sendUserActivation(final String email, final URI baseUri) throws Exception {
		User user = getUser(email);
		final String token;

		if (user.getConfirmationToken() == null) {
			token = Passwords.randomToken();
			user.setConfirmationToken(Passwords.hash(token, email));
			userDAO.update(user);
		} else {
			token = user.getConfirmationToken();
		}

		String content = Strings.parse(Reflections.getResourceAsStream("email/activation.html"));
		content = content.replace("{name}", user.getProfile().getName());
		content = content.replace("{url}", baseUri.resolve("activation?token=" + token).toString());

		send("Portal FBCA - Confirmação de e-mail", content, "text/html", email);
	}

	public void sendWelcome(final String email, final URI baseUri) throws Exception {
		User user = getUser(email);

		String content = Strings.parse(Reflections.getResourceAsStream("email/welcome.html"));
		content = content.replace("{name}", user.getProfile().getName());
		content = content.replace("{url}", baseUri.toString());

		send("Portal FBCA - Seja bem-vindo!", content, "text/html", email);
	}

	public void sendPasswordCreationMail(final String email, final URI baseUri) throws Exception {
		User user = getUser(email);
		final String token;

		if (user.getPasswordResetToken() == null) {
			token = Passwords.randomToken();
			user.setPasswordResetToken(Passwords.hash(token, email));
			user.setPasswordResetRequest(new Date());
			userDAO.update(user);
		} else {
			token = user.getPasswordResetToken();
		}

		String content = Strings.parse(Reflections.getResourceAsStream("email/password-creation.html"));
		content = content.replace("{name}", user.getProfile().getName());
		content = content.replace("{url}", baseUri.resolve("password/reset?token=" + token).toString());
		send("Portal FBCA - Criação de senha", content, "text/html", email);
	}

	public void sendResetPasswordMail(final String email, final URI baseUri) throws Exception {
		User user = getUser(email);
		final String token;

		if (user.getPasswordResetToken() == null) {
			token = Passwords.randomToken();
			user.setPasswordResetToken(Passwords.hash(token, email));
			user.setPasswordResetRequest(new Date());
			userDAO.update(user);
		} else {
			token = user.getPasswordResetToken();
		}

		String content = Strings.parse(Reflections.getResourceAsStream("email/password-recovery.html"));
		content = content.replace("{name}", user.getProfile().getName());
		content = content.replace("{url}", baseUri.resolve("password/reset?token=" + token).toString());
		send("Portal FBCA - Recuperação de senha", content, "text/html", email);
	}

	public void sendRegisterCreation(Register register, List<User> members, URI baseUri) throws Exception {
		User creator = getUser(register.getCreator().getEmail());
		register = Beans.getReference(RegisterDAO.class).loadForEmail(register.getId());

		String[] memberNames = new String[members.size()];
		for (int i = 0; i < members.size(); i++) {
			// TODO Não tem necessidade de novo select. Já poderia vir preenchido
			Profile profile = Beans.getReference(ProfileDAO.class).load(members.get(i));
			memberNames[i] = profile.getName();
		}

		String content = Strings.parse(Reflections.getResourceAsStream("email/register-creation.html"));
		content = content.replace("{name}", creator.getProfile().getName());
		content = content.replace("{teamName}", register.getTeamName());
		content = content.replace("{raceName}", register.getRaceCategory().getRace().getName());
		content = content.replace("{raceDate}", Dates.parse(register.getRaceCategory().getRace().getDate()));
		content = content.replace("{url}", baseUri.resolve("register/" + register.getId()).toString());
		content = content.replace("{registerId}", register.getFormattedId());
		content = content.replace("{registerDate}", Dates.parse(register.getDate()));
		content = content.replace("{categoryName}", register.getRaceCategory().getCategory().getName());
		content = content.replace("{courseLength}", register.getRaceCategory().getCourse().getLength().toString());
		content = content.replace("{members}", Strings.join(" / ", memberNames));

		Period period = Beans.getReference(PeriodDAO.class).loadCurrent(register.getRaceCategory().getRace());
		content = content.replace("{racePrice}", period.getPrice().toString().replace(".", ","));

		String subject = "Portal FBCA - Pedido de inscrição";
		subject += " #" + register.getFormattedId();
		subject += " - " + register.getRaceCategory().getRace().getName();

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

	private User getUser(String email) {
		User user = userDAO.loadFull(email);

		if (user == null) {
			throw new IllegalStateException("Nenhuma conta associada ao e-mail " + email);
		}

		return user;
	}

	private Session getSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", config.getHost());
		props.put("mail.smtp.auth", "true");

		if (config.getPort() != null) {
			props.put("mail.smtp.port", config.getPort());
			props.put("mail.smtp.socketFactory.port", config.getPort());
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}

		if (config.getTls() != null) {
			props.put("mail.smtp.starttls.enable", config.getTls());
		}

		Authenticator authenticator = new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(config.getUser(), config.getPassword());
			}
		};

		return Session.getInstance(props, authenticator);
	}
}
