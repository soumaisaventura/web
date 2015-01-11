package adventure.persistence;

import static javax.mail.Message.RecipientType.TO;

import java.net.URI;
import java.util.Date;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import adventure.entity.Account;
import adventure.security.Passwords;
import adventure.util.ApplicationConfig;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class MailDAO {

	@Inject
	private ApplicationConfig config;

	@Inject
	private AccountDAO accountDAO;

	public void sendAccountActivationMail(final String email, final URI baseUri) throws MessagingException {
		Account account = getAccount(email);
		final String token;

		if (account.getConfirmationToken() == null) {
			token = Passwords.randomToken();
			account.setConfirmationToken(Passwords.hash(token, email));
			accountDAO.update(account);
		} else {
			token = account.getConfirmationToken();
		}

		new Thread() {

			public void run() {
				try {
					MimeMessage message = new MimeMessage(getSession());
					message.setFrom(new InternetAddress("contato@fbca.com.br"));
					message.setSubject("Ativação de conta");
					message.setRecipients(TO, email);
					message.setContent(baseUri.resolve("activation?token=" + token).toString(), "text/plain");

					Transport.send(message);

				} catch (MessagingException cause) {
					cause.printStackTrace();
				}
			};

		}.start();
	}

	public void sendPasswordCreationMail(final String email, final URI baseUri) throws MessagingException {
		Account account = getAccount(email);
		final String token;

		if (account.getPasswordResetToken() == null) {
			token = Passwords.randomToken();
			account.setPasswordResetToken(Passwords.hash(token, email));
			account.setPasswordResetRequest(new Date());
			accountDAO.update(account);
		} else {
			token = account.getPasswordResetToken();
		}

		new Thread() {

			public void run() {
				try {
					MimeMessage message = new MimeMessage(getSession());
					message.setFrom(new InternetAddress("contato@fbca.com.br"));
					message.setSubject("Criação de senha");
					message.setRecipients(TO, email);
					message.setContent(baseUri.resolve("password?token=" + token).toString(), "text/plain");

					Transport.send(message);

				} catch (MessagingException cause) {
					cause.printStackTrace();
				}
			};

		}.start();
	}

	public void sendResetPasswordMail(final String email, final URI baseUri) throws MessagingException {
		Account account = getAccount(email);
		final String token;

		if (account.getPasswordResetToken() == null) {
			token = Passwords.randomToken();
			account.setPasswordResetToken(Passwords.hash(token, email));
			account.setPasswordResetRequest(new Date());
			accountDAO.update(account);
		} else {
			token = account.getPasswordResetToken();
		}

		new Thread() {

			public void run() {
				try {
					MimeMessage message = new MimeMessage(getSession());
					message.setFrom(new InternetAddress("contato@fbca.com.br"));
					message.setSubject("Redefinição de senha");
					message.setRecipients(TO, email);
					message.setContent(baseUri.resolve("password?token=" + token).toString(), "text/plain");

					Transport.send(message);

				} catch (MessagingException cause) {
					cause.printStackTrace();
				}
			};

		}.start();
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
