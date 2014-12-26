package br.com.fbca.persistence;

import static javax.mail.Message.RecipientType.TO;

import java.net.URI;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.infinispan.Cache;

import br.com.fbca.security.Passwords;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Singleton
@Transactional
public class MailDAO {

	@Inject
	private ContainerResources resources;

	@Asynchronous
	public void sendResetPasswordMail(String email, URI baseUri) throws MessagingException {
		Cache<String, String> cache = Beans.getReference(ContainerResources.class).getPasswordResetCache();
		String token = cache.get(email);

		if (token == null) {
			token = Passwords.randomToken();
			cache.put(email, token);
		}

		MimeMessage message = new MimeMessage(resources.getSession());

		message.setFrom(new InternetAddress("contato@fbca.com.br"));
		message.setSubject("Redefinição de senha");
		message.setRecipients(TO, email);
		message.setContent(baseUri.resolve("password?token=" + token).toString(), "text/plain");

		Transport.send(message);
	}
}
