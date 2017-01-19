package adventure.persistence;

import adventure.entity.Category;
import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class CategoryDAO extends JPACrud<Category, Integer> {

    private static final long serialVersionUID = 1L;

    public static CategoryDAO getInstance() {
        return Beans.getReference(CategoryDAO.class);
    }

    @Deprecated
    public List<Category> find(Race race) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new Category ( ";
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
        jpql += "        c.maxTeamAge ";
        jpql += "        ) ";
        jpql += "   from RaceCategory rc ";
        jpql += "   join rc.category c ";
        jpql += "  where rc.race = :race ";
        jpql += "  order by ";
        jpql += "        c.teamSize desc, ";
        jpql += "        (case when c.minFemaleMembers is null then 0 else c.minFemaleMembers end) desc, ";
        jpql += "        (case when c.minMaleMembers is null then 0 else c.minMaleMembers end) desc, ";
        jpql += "        (case when c.minMemberAge is null then c.maxMemberAge else c.minMemberAge end) desc, ";
        jpql += "        (case when c.minTeamAge is null then c.maxTeamAge else c.minTeamAge end) desc, ";
        jpql += "        c.name ";

        TypedQuery<Category> query = getEntityManager().createQuery(jpql, Category.class);
        query.setParameter("race", race);

        return query.getResultList();
    }
}
