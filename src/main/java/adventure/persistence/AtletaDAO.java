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

	public List<Atleta> findByCpf(String cpf) {
		String jpql = "from Atleta a where a.cpf = :cpf";

		TypedQuery<Atleta> query = getEntityManager().createQuery(jpql, Atleta.class);
		query.setParameter("cpf", cpf);

		return query.getResultList();
	}

	public List<Atleta> findByRg(String rg) {
		String jpql = "from Atleta a where a.rg = :rg";

		TypedQuery<Atleta> query = getEntityManager().createQuery(jpql, Atleta.class);
		query.setParameter("rg", rg);

		return query.getResultList();
	}
}
