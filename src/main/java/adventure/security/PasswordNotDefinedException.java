package adventure.security;

import br.gov.frameworkdemoiselle.security.AuthenticationException;

public class PasswordNotDefinedException extends AuthenticationException {

	public PasswordNotDefinedException() {
		super("Você ainda não definiu uma senha para esta conta");
	}

	private static final long serialVersionUID = 1L;

}
