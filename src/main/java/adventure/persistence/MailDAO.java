package adventure.persistence;

import static javax.mail.Message.RecipientType.TO;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.hibernate.validator.constraints.Email;

@Singleton
public class MailDAO {

	@Resource(mappedName = "java:/mail/Adventure")
	private Session mailSession;

	@Asynchronous
	public void sendMail(@Email String to) throws MessagingException {
		MimeMessage message = new MimeMessage(mailSession);
		message.setRecipients(TO, to);
		message.setSubject("Your subject");
		message.setContent("Your message", "text/plain");

		Transport.send(message);
	}
}
