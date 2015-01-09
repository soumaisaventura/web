package adventure.security;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class ConfirmationTokenSession {

	private String value;

	public boolean isEmpty() {
		return getValue() == null;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
