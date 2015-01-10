package adventure.persistence;

import adventure.entity.Register;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class RegisterDAO extends JPACrud<Register, Long> {

	private static final long serialVersionUID = 1L;
}
