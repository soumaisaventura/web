package adventure.util;

import org.hibernate.validator.constraints.NotEmpty;

import br.gov.frameworkdemoiselle.annotation.Name;
import br.gov.frameworkdemoiselle.configuration.Configuration;

@Configuration(resource = "application")
public class ApplicationConfig {

	@Name("title")
	private String title = "Adventure";

	@NotEmpty
	@Name("mail.smtp.host")
	private String host;

	@Name("mail.smtp.port")
	private Integer port;

	@NotEmpty
	@Name("mail.smtp.user")
	private String user;

	@NotEmpty
	@Name("mail.smtp.password")
	private String password;

	@Name("mail.smtp.starttls.enable")
	private Boolean tls;

	public String getTitle() {
		return title;
	}

	public String getHost() {
		return host;
	}

	public Integer getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getTls() {
		return tls;
	}
}
