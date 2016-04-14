package adventure.persistence;

import adventure.entity.Kit;
import adventure.entity.Race;
import adventure.entity.UserRegistration;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        jpql += "        k.price desc ";

        TypedQuery<Kit> query = em.createQuery(jpql, Kit.class);
        query.setParameter("race", race);

        return query.getResultList();
    }

    public Kit loadForRegistration(Race race, String alias) {
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
        jpql += "    and k.alias = :kitAlias ";

        TypedQuery<Kit> query = em.createQuery(jpql, Kit.class);
        query.setParameter("race", race);
        query.setParameter("kitAlias", alias);

        Kit result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    public Kit loadForRegistration(UserRegistration userRegistration) {
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
        jpql += "   from UserRegistration ur ";
        jpql += "   join ur.kit k ";
        jpql += "   join k.race r ";
        jpql += "  where ur.user = :user ";
        jpql += "    and ur.registration = :registration ";

        TypedQuery<Kit> query = em.createQuery(jpql, Kit.class);
        query.setParameter("user", userRegistration.getUser());
        query.setParameter("registration", userRegistration.getRegistration());

        Kit result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }
}
