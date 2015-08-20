package adventure.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.City;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class CityDAO extends JPACrud<City, Integer> {

	private static final long serialVersionUID = 1L;

	public static CityDAO getInstance() {
		return Beans.getReference(CityDAO.class);
	}

	public List<City> loadByState(int idState) {
		
		StringBuffer jpql = new StringBuffer();
		jpql.append("select c from City c join c.state s where s.id = :idState order by c.name");
		
		TypedQuery<City> query = getEntityManager().createQuery(jpql.toString(), City.class);
		query.setParameter("idState", idState);

		return query.getResultList();
	}
}
