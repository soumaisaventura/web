package adventure.persistence;

import adventure.entity.City;
import adventure.entity.State;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class CityDAO extends JPACrud<City, Integer> {

    private static final long serialVersionUID = 1L;

    public static CityDAO getInstance() {
        return Beans.getReference(CityDAO.class);
    }

    public List<City> find(State state) {
        String jpql = "select c from City c where c.state = :state order by c.name";
        TypedQuery<City> query = getEntityManager().createQuery(jpql, City.class);
        query.setParameter("state", state);

        return query.getResultList();
    }

    /*
     * TODO Apagar na v2
     */
    @Deprecated
    public List<City> search(String filter) {
        String jpql = "";
        jpql += " select ";
        jpql += " 	new City( ";
        jpql += " 	    c.id, ";
        jpql += " 	    c.name, ";
        jpql += " 	    s.name, ";
        jpql += " 	    s.abbreviation, ";
        jpql += " 	    o.name ";
        jpql += " 	    ) ";
        jpql += "  from City c ";
        jpql += "  join c.state s ";
        jpql += "  join s.country o ";
        jpql += " where lower(c.name) like :filter ";
        jpql += " order by ";
        jpql += "       s.id, ";
        jpql += "       c.name ";

        TypedQuery<City> query = getEntityManager().createQuery(jpql, City.class);
        query.setMaxResults(10);
        query.setParameter("filter", Strings.isEmpty(filter) ? "" : "%" + filter.toLowerCase() + "%");

        return query.getResultList();
    }
}
