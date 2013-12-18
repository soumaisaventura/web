package adventure.security;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class Credentials {

	private String email;

	private String password;

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
