package br.com.fbca.persistence;

import br.com.fbca.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class RaceDAO extends JPACrud<User, Long> {

	private static final long serialVersionUID = 1L;

}
