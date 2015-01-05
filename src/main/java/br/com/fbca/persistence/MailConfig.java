package br.com.fbca.persistence;

import org.hibernate.validator.constraints.NotEmpty;

import br.gov.frameworkdemoiselle.annotation.Name;
import br.gov.frameworkdemoiselle.configuration.Configuration;

@Configuration
public class MailConfig {

	@NotEmpty
	@Name("mail.smtp.host")
	private String host;

	@NotEmpty
	@Name("mail.smtp.user")
	private String user;

	@NotEmpty
	@Name("mail.smtp.password")
	private String password;

	public String getHost() {
		return host;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}
