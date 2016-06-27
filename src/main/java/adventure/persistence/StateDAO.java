package adventure.persistence;

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


    @Override
    public List<State> findAll() {
        String jpql = "";
        jpql += " select ";
        jpql += "    new State( ";
        jpql += "        s.id, ";
        jpql += "        s.name, ";
        jpql += "        s.abbreviation ";
        jpql += "        ) ";
        jpql += "   from State s ";
        jpql += "  order by s.abbreviation ";

        TypedQuery<State> query = getEntityManager().createQuery(jpql, State.class);

        return super.findAll();
    }

    public State load(String abbreviation) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new State( ";
        jpql += "        s.id, ";
        jpql += "        s.name, ";
        jpql += "        s.abbreviation ";
        jpql += "        ) ";
        jpql += "   from State s ";
        jpql += "  where s.abbreviation = :abbreviation ";
        jpql += "  order by s.abbreviation ";

        TypedQuery<State> query = getEntityManager().createQuery(jpql, State.class);
        query.setParameter("abbreviation", abbreviation);

        State result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;

    }

}
