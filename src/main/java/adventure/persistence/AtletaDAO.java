package adventure.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Atleta;
import br.gov.frameworkdemoiselle.template.JPACrud;

public class AtletaDAO extends JPACrud<Atleta, Long> {

	private static final long serialVersionUID = 1L;

	public List<Atleta> findByEmail(String email) {
		String jpql = "from Atleta a where a.email = :email";

		TypedQuery<Atleta> query = getEntityManager().createQuery(jpql, Atleta.class);
		query.setParameter("email", email);

		return query.getResultList();
	}
}
