package br.com.fbca.persistence;

import br.com.fbca.entity.Athlete;
import br.com.fbca.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class PersonalDataDAO extends JPACrud<Athlete, User> {

	private static final long serialVersionUID = 1L;

}
