package adventure.persistence;

import static javax.mail.Message.RecipientType.TO;

import java.net.URI;
import java.util.Date;
import java.util.Properties;

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

//@Singleton
@Transactional
public class MailDAO {

	@Inject
	private ApplicationConfig config;

	@Inject
	private AccountDAO accountDAO;

	// @Asynchronous
	public void sendAccountActivationMail(String email, URI baseUri) throws MessagingException {
		Account account = getAccount(email);
		String token = account.getActivationToken();

		if (token == null) {
			token = Passwords.randomToken();
			account.setActivationToken(token);
			accountDAO.update(account);
		}

		MimeMessage message = new MimeMessage(getSession());
		message.setFrom(new InternetAddress("contato@fbca.com.br"));
		message.setSubject("Ativação de conta");
		message.setRecipients(TO, email);
		message.setContent(baseUri.resolve("activation?token=" + token).toString(), "text/plain");

		Transport.send(message);
	}

	// @Asynchronous
	public void sendPasswordCreationMail(String email, URI baseUri) throws MessagingException {
		Account account = getAccount(email);
		String token = account.getPasswordResetToken();

		if (token == null) {
			token = Passwords.randomToken();
			account.setPasswordResetToken(token);
			account.setPasswordResetRequest(new Date());
			accountDAO.update(account);
		}

		MimeMessage message = new MimeMessage(getSession());
		message.setFrom(new InternetAddress("contato@fbca.com.br"));
		message.setSubject("Criação de senha");
		message.setRecipients(TO, email);
		message.setContent(baseUri.resolve("password?token=" + token).toString(), "text/plain");

		Transport.send(message);
	}

	// @Asynchronous
	public void sendResetPasswordMail(String email, URI baseUri) throws MessagingException {
		Account account = getAccount(email);
		String token = account.getPasswordResetToken();

		if (token == null) {
			token = Passwords.randomToken();
			account.setPasswordResetToken(token);
			account.setPasswordResetRequest(new Date());
			accountDAO.update(account);
		}

		MimeMessage message = new MimeMessage(getSession());
		message.setFrom(new InternetAddress("contato@fbca.com.br"));
		message.setSubject("Redefinição de senha");
		message.setRecipients(TO, email);
		message.setContent(baseUri.resolve("password?token=" + token).toString(), "text/plain");

		Transport.send(message);
	}

	private Account getAccount(String email) {
		Account account = accountDAO.load(email);

		if (account == null) {
			throw new IllegalStateException("Nenhuma conta associada ao e-mail " + email);
		}

		return account;
	}

	private Session getSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", config.getHost());
		props.put("mail.smtp.user", config.getUser());
		props.put("mail.smtp.password", config.getPassword());
		// props.put("mail.smtp.socketFactory.fallback", "false");
		// props.put("mail.smtp.auth", "true");

		if (config.getPort() != null) {
			props.put("mail.smtp.port", config.getPort());
			// props.put("mail.smtp.socketFactory.port", config.getPort());
			// props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}

		if (config.getTls() != null) {
			// props.put("mail.smtp.starttls.enable", config.getTls());
		}

		return Session.getInstance(props);
	}
}
