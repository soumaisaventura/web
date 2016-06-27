package adventure.persistence;

import adventure.entity.Country;
import adventure.entity.State;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class CountryDAO extends JPACrud<Country, Integer> {

    private static final long serialVersionUID = 1L;

    public static CountryDAO getInstance() {
        return Beans.getReference(CountryDAO.class);
    }

    @Override
    public List<Country> findAll() {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Country( ";
        jpql += "        y.id, ";
        jpql += "        y.name, ";
        jpql += "        y.abbreviation ";
        jpql += "        ) ";
        jpql += "   from Country y ";
        jpql += "  order by y.abbreviation ";

        TypedQuery<Country> query = getEntityManager().createQuery(jpql, Country.class);

        return query.getResultList();
    }

    public Country load(String abbreviation) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Country( ";
        jpql += "        y.id, ";
        jpql += "        y.name, ";
        jpql += "        y.abbreviation ";
        jpql += "        ) ";
        jpql += "   from Country y ";
        jpql += "  where y.abbreviation = :abbreviation ";
        jpql += "  order by y.abbreviation ";

        TypedQuery<Country> query = getEntityManager().createQuery(jpql, Country.class);
        query.setParameter("abbreviation", abbreviation.toUpperCase());

        Country result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;

    }

}
