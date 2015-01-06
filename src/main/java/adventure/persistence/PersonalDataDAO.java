package adventure.persistence;

import adventure.entity.Athlete;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class PersonalDataDAO extends JPACrud<Athlete, User> {

	private static final long serialVersionUID = 1L;

}
