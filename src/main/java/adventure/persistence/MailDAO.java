package adventure.persistence;

import static javax.mail.Message.RecipientType.TO;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.hibernate.validator.constraints.Email;

@Singleton
public class MailDAO {

	@Inject
	private ContainerResources resources;

	@Asynchronous
	public void sendResetPasswordMail(@Email String email, String token) throws MessagingException {
		MimeMessage message = new MimeMessage(resources.getSession());

		message.setSubject("Redefinição de senha");
		message.setRecipients(TO, email);
		message.setContent("E-mail: " + email + "\nToken: " + token, "text/plain");

		Transport.send(message);
	}
}
