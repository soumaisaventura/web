package adventure.persistence;

import adventure.entity.Modality;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class ModalityDAO extends JPACrud<Modality, Long> {

	private static final long serialVersionUID = 1L;
}
