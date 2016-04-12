package adventure.persistence;

import adventure.entity.Modality;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Transactional
public class ModalityDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public static ModalityDAO getInstance() {
        return Beans.getReference(ModalityDAO.class);
    }

    public List<Modality> findForEvent(Race race) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Modality ( ";
        jpql += "        m.id, ";
        jpql += "        m.name, ";
        jpql += "        m.alias ";
        jpql += "        ) ";
        jpql += "   from RaceModality rm ";
        jpql += "   join rm.modality m ";
        jpql += "  where rm.race = :race ";
        jpql += "  order by ";
        jpql += "        m.id ";

        TypedQuery<Modality> query = em.createQuery(jpql, Modality.class);
        query.setParameter("race", race);

        return query.getResultList();
    }

    // TODO: OLD
}
