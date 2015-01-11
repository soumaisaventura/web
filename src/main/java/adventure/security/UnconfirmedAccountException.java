package adventure.security;

import br.gov.frameworkdemoiselle.security.AuthenticationException;

public class UnconfirmedAccountException extends AuthenticationException {

	public UnconfirmedAccountException() {
		super("Esta conta ainda não foi ativada.");
	}

	private static final long serialVersionUID = 1L;

}
