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
public class StateDAO extends JPACrud<State, Integer> {

    private static final long serialVersionUID = 1L;

    public static StateDAO getInstance() {
        return Beans.getReference(StateDAO.class);
    }

    public List<State> find(Country country) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new State( ";
        jpql += "        s.id, ";
        jpql += "        s.name, ";
        jpql += "        s.abbreviation, ";
        jpql += "        y.id, ";
        jpql += "        y.name, ";
        jpql += "        y.abbreviation ";
        jpql += "        ) ";
        jpql += "   from State s ";
        jpql += "   join s.country y ";
        jpql += "  where y = :country ";
        jpql += "  order by s.abbreviation ";

        TypedQuery<State> query = getEntityManager().createQuery(jpql, State.class);
        query.setParameter("country", country);

        return query.getResultList();
    }

    public State load(String abbreviation, String countryAbbreviation) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new State( ";
        jpql += "        s.id, ";
        jpql += "        s.name, ";
        jpql += "        s.abbreviation, ";
        jpql += "        y.id, ";
        jpql += "        y.name, ";
        jpql += "        y.abbreviation ";
        jpql += "        ) ";
        jpql += "   from State s ";
        jpql += "   join s.country y ";
        jpql += "  where y.abbreviation = :countryAbbreviation ";
        jpql += "    and s.abbreviation = :abbreviation ";
        jpql += "  order by s.abbreviation ";

        TypedQuery<State> query = getEntityManager().createQuery(jpql, State.class);
        query.setParameter("abbreviation", abbreviation.toUpperCase());
        query.setParameter("countryAbbreviation", countryAbbreviation.toUpperCase());

        State result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }
}
