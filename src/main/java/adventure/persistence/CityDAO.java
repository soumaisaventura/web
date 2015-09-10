package adventure.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.City;
import adventure.entity.State;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

@Transactional
public class CityDAO extends JPACrud<City, Integer> {

	private static final long serialVersionUID = 1L;

	public static CityDAO getInstance() {
		return Beans.getReference(CityDAO.class);
	}

	public List<City> find(State state) {

		StringBuffer jpql = new StringBuffer();
		jpql.append("select c from City c where c.state = :state order by c.name");

		TypedQuery<City> query = getEntityManager().createQuery(jpql.toString(), City.class);
		query.setParameter("state", state);

		return query.getResultList();
	}

	/*
	 * TODO Apagar na v2
	 */
	@Deprecated
	public List<City> search(String filter) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select ");
		jpql.append(" 	new City( ");
		jpql.append(" 	    c.id, ");
		jpql.append(" 	    c.name, ");
		jpql.append(" 	    s.name, ");
		jpql.append(" 	    s.abbreviation, ");
		jpql.append(" 	    o.name ");
		jpql.append(" 	    ) ");
		jpql.append("  from City c ");
		jpql.append("  join c.state s ");
		jpql.append("  join s.country o ");
		jpql.append(" where lower(c.name) like :filter ");
		jpql.append(" order by ");
		jpql.append("       s.id, ");
		jpql.append("       c.name ");

		TypedQuery<City> query = getEntityManager().createQuery(jpql.toString(), City.class);
		query.setMaxResults(10);
		query.setParameter("filter", Strings.isEmpty(filter) ? "" : "%" + filter.toLowerCase() + "%");

		return query.getResultList();
	}
}
