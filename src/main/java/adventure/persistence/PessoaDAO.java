package adventure.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Pessoa;
import br.gov.frameworkdemoiselle.template.JPACrud;

public class PessoaDAO extends JPACrud<Pessoa, Long> {

	private static final long serialVersionUID = 1L;

	public List<Pessoa> findByEmail(String email) {
		String jpql = "from " + this.getBeanClass().getName() + " where email = :email";

		TypedQuery<Pessoa> query = getEntityManager().createQuery(jpql, Pessoa.class);
		query.setParameter("email", email);

		return query.getResultList();
	}
}
