package adventure.security;

import javax.enterprise.context.RequestScoped;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import adventure.entity.JSEntity;

@JSEntity
@RequestScoped
public class Credentials {

	@Email
	@NotEmpty
	private String email;

	@NotEmpty
	private String password;

	@JsonIgnore
	private boolean oauth = false;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isOauth() {
		return oauth;
	}

	public void setOauth(boolean oauth) {
		this.oauth = oauth;
	}
}
