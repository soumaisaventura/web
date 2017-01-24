package core.persistence;

import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import core.entity.Health;
import core.entity.User;
import temp.util.PendencyCounter;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Transactional
public class HealthDAO extends JPACrud<Health, User> {

    private static final long serialVersionUID = 1L;

    public static HealthDAO getInstance() {
        return Beans.getReference(HealthDAO.class);
    }

    @Override
    public void delete(User id) {
        getEntityManager().remove(load(id));
    }

    @Override
    public Health insert(Health health) {
        health.setPendencies(PendencyCounter.count(health));
        return super.insert(health);
    }

    @Override
    public Health update(Health health) {
        health.setPendencies(PendencyCounter.count(health));
        return super.update(health);
    }

    @Override
    public Health load(User user) {
        String jpql = " from Health h where h.user = :user ";
        TypedQuery<Health> query = getEntityManager().createQuery(jpql, Health.class);
        query.setParameter("user", user);

        Health result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }
}
