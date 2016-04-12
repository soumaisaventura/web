package adventure.persistence;

import adventure.entity.Kit;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Transactional
public class KitDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public static KitDAO getInstance() {
        return Beans.getReference(KitDAO.class);
    }

    public List<Kit> findForRegistration(Race race) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Kit ( ";
        jpql += "        k.id, ";
        jpql += "        k.alias, ";
        jpql += "        k.name, ";
        jpql += "        k.description, ";
        jpql += "        k.price, ";
        jpql += "        r.id ";
        jpql += "        ) ";
        jpql += "   from Kit k ";
        jpql += "   join k.race r ";
        jpql += "  where r = :race ";
        jpql += "  order by ";
        jpql += "        r.id, ";
        jpql += "        k.price ";

        TypedQuery<Kit> query = em.createQuery(jpql, Kit.class);
        query.setParameter("race", race);

        return query.getResultList();
    }
}
