package br.com.fbca.persistence_;

import br.com.fbca.entity_.Modality;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class ModalityDAO extends JPACrud<Modality, Long> {

	private static final long serialVersionUID = 1L;
}
