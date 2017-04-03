package core.security;

import br.gov.frameworkdemoiselle.security.AuthenticationException;

public class UnconfirmedUserException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UnconfirmedUserException() {
        super("Esta conta ainda não foi ativada.");
    }

}
