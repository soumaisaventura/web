package adventure.security;

import br.gov.frameworkdemoiselle.security.Authorizer;

public class Autorizador implements Authorizer {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean hasRole(String role) throws Exception {
		return false;
	}

	@Override
	public boolean hasPermission(String resource, String operation) throws Exception {
		return false;
	}
}
