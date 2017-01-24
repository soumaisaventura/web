package temp.security;

import br.gov.frameworkdemoiselle.security.AuthenticationException;

public class PasswordNotDefinedException extends AuthenticationException {


    private static final long serialVersionUID = 1L;

    public PasswordNotDefinedException() {
        super("Você ainda não definiu uma senha para esta conta.");
    }

}
