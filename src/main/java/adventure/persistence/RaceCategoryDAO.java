package adventure.persistence;

import adventure.entity.Race;
import adventure.entity.RaceCategory;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Transactional
public class RaceCategoryDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public static RaceCategoryDAO getInstance() {
        return Beans.getReference(RaceCategoryDAO.class);
    }

    public RaceCategory load(Integer raceId, String categoryAlias) throws Exception {
        String jpql = "";
        jpql += " select ";
        jpql += "    new RaceCategory( ";
        jpql += "        c.id, ";
        jpql += "        c.alias, ";
        jpql += "        c.name, ";
        jpql += "        c.description, ";
        jpql += "        c.teamSize, ";
        jpql += "        c.minMaleMembers, ";
        jpql += "        c.minFemaleMembers, ";
        jpql += "        c.minMemberAge, ";
        jpql += "        c.maxMemberAge, ";
        jpql += "        c.minTeamAge, ";
        jpql += "        c.maxTeamAge, ";
        jpql += "        r.id, ";
        jpql += "        r.name, ";
        jpql += "        r.period.beginning, ";
        jpql += "        r.period.end, ";
        jpql += "        rc.vacant ";
        jpql += "        ) ";
        jpql += "   from RaceCategory rc ";
        jpql += "   join rc.race r ";
        jpql += "   join rc.category c ";
        jpql += "  where r.id = :raceId ";
        jpql += "    and c.alias = :categoryAlias ";

        TypedQuery<RaceCategory> query = em.createQuery(jpql, RaceCategory.class);
        query.setParameter("raceId", raceId);
        query.setParameter("categoryAlias", categoryAlias);

        RaceCategory result;

        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }

        return result;
    }

    public List<RaceCategory> find(Race race) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new RaceCategory( ";
        jpql += "        c.id, ";
        jpql += "        c.alias, ";
        jpql += "        c.name, ";
        jpql += "        c.description, ";
        jpql += "        c.teamSize, ";
        jpql += "        c.minMaleMembers, ";
        jpql += "        c.minFemaleMembers, ";
        jpql += "        c.minMemberAge, ";
        jpql += "        c.maxMemberAge, ";
        jpql += "        c.minTeamAge, ";
        jpql += "        c.maxTeamAge, ";
        jpql += "        r.id, ";
        jpql += "        r.name, ";
        jpql += "        r.period.beginning, ";
        jpql += "        r.period.end, ";
        jpql += "        rc.vacant ";
        jpql += "        ) ";
        jpql += "   from RaceCategory rc ";
        jpql += "   join rc.race r ";
        jpql += "   join rc.category c ";
        jpql += "  where r = :race ";
        jpql += "  order by ";
        jpql += "        c.teamSize desc, ";
        jpql += "        c.name ";

        TypedQuery<RaceCategory> query = em.createQuery(jpql, RaceCategory.class);
        query.setParameter("race", race);

        return query.getResultList();

    }
}
