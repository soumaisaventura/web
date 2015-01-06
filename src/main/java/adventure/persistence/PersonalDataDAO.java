package adventure.persistence;

import adventure.entity.Athlete;
import adventure.entity.Account;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class PersonalDataDAO extends JPACrud<Athlete, Account> {

	private static final long serialVersionUID = 1L;

}
