package adventure.persistence;

import static javax.mail.Message.RecipientType.TO;

import java.net.URI;
import java.util.Date;
import java.util.Properties;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import adventure.entity.Account;
import adventure.security.Passwords;
import adventure.util.ApplicationConfig;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Singleton
@Transactional
public class MailDAO {

	@Inject
	private ApplicationConfig config;

	@Inject
	private AccountDAO userDAO;

	@Asynchronous
	public void sendAccountActivationMail(String email, URI baseUri) throws MessagingException {
		Account user = getUser(email);
		String token = user.getActivationToken();

		if (token == null) {
			token = Passwords.randomToken();
			user.setActivationToken(token);
			userDAO.update(user);
		}

		MimeMessage message = new MimeMessage(getSession());
		message.setFrom(new InternetAddress("contato@fbca.com.br"));
		message.setSubject("Ativação de conta");
		message.setRecipients(TO, email);
		message.setContent(baseUri.resolve("activation?token=" + token).toString(), "text/plain");

		Transport.send(message);
	}

	@Asynchronous
	public void sendPasswordCreationMail(String email, URI baseUri) throws MessagingException {
		Account user = getUser(email);
		String token = user.getPasswordResetToken();

		if (token == null) {
			token = Passwords.randomToken();
			user.setPasswordResetToken(token);
			user.setPasswordResetRequest(new Date());
			userDAO.update(user);
		}

		MimeMessage message = new MimeMessage(getSession());
		message.setFrom(new InternetAddress("contato@fbca.com.br"));
		message.setSubject("Criação de senha");
		message.setRecipients(TO, email);
		message.setContent(baseUri.resolve("password?token=" + token).toString(), "text/plain");

		Transport.send(message);
	}

	@Asynchronous
	public void sendResetPasswordMail(String email, URI baseUri) throws MessagingException {
		Account user = getUser(email);
		String token = user.getPasswordResetToken();

		if (token == null) {
			token = Passwords.randomToken();
			user.setPasswordResetToken(token);
			user.setPasswordResetRequest(new Date());
			userDAO.update(user);
		}

		MimeMessage message = new MimeMessage(getSession());
		message.setFrom(new InternetAddress("contato@fbca.com.br"));
		message.setSubject("Redefinição de senha");
		message.setRecipients(TO, email);
		message.setContent(baseUri.resolve("password?token=" + token).toString(), "text/plain");

		Transport.send(message);
	}

	private Account getUser(String email) {
		Account user = userDAO.load(email);

		if (user == null) {
			// TODO Lançar exceção
			throw new IllegalStateException("Nenhum usuário associado ao e-mail " + email);
		}

		return user;
	}

	private Session getSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", config.getHost());
		props.put("mail.smtp.user", config.getUser());
		props.put("mail.smtp.password", config.getPassword());

		if (config.getPort() != null) {
			props.put("mail.smtp.port", config.getPort());
		}

		return Session.getInstance(props);
	}
}
