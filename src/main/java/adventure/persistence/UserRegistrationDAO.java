package adventure.persistence;

import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.User;
import adventure.entity.UserRegistration;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

import static adventure.entity.RegistrationStatusType.CANCELLED;

@Transactional
public class UserRegistrationDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public static UserRegistrationDAO getInstance() {
        return Beans.getReference(UserRegistrationDAO.class);
    }

    public UserRegistration insert(UserRegistration entity) {
        em.persist(entity);
        return entity;
    }

    public UserRegistration update(UserRegistration entity) {
        em.merge(entity);
        return entity;
    }

    public List<UserRegistration> find(Registration registration) {
        String jpql = "";
        jpql += "    select ";
        jpql += "       new UserRegistration( ";
        jpql += "           r.id, ";
        jpql += "           u.id, ";
        jpql += "           u.email, ";
        jpql += " 	        pr.name, ";
        jpql += " 	        pr.gender, ";
        jpql += " 	        pr.mobile, ";
        jpql += "           tf.amount, ";
        jpql += "           k.id, ";
        jpql += "           k.alias, ";
        jpql += "           k.name, ";
        jpql += "           k.description ";
        jpql += "           ) ";
        jpql += "      from UserRegistration tf ";
        jpql += " left join tf.kit k ";
        jpql += "      join tf.user u ";
        jpql += "      join tf.registration r, ";
        jpql += "           Profile pr ";
        jpql += "     where u = pr.user ";
        jpql += "       and r = :registration ";
        jpql += "     order by ";
        jpql += "           pr.name ";

        TypedQuery<UserRegistration> query = em.createQuery(jpql, UserRegistration.class);
        query.setParameter("registration", registration);

        return query.getResultList();
    }

    public UserRegistration load(Registration registration, User user) {
        String jpql = "";
        jpql += " select tf ";
        jpql += "   from UserRegistration tf ";
        jpql += "  where tf.registration = :registration ";
        jpql += "    and tf.user = :user ";

        TypedQuery<UserRegistration> query = em.createQuery(jpql, UserRegistration.class);
        query.setParameter("registration", registration);
        query.setParameter("user", user);

        UserRegistration result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    @Transactional
    public void delete(Registration registration, User user) {
        String jpql = "";
        jpql += " delete UserRegistration ur ";
        jpql += "  where ur.registration = :registration ";
        jpql += "    and ur.user = :user ";

        Query query = em.createQuery(jpql);
        query.setParameter("registration", registration);
        query.setParameter("user", user);

        query.executeUpdate();
    }

    public UserRegistration loadForRegistrationSubmissionValidation(Race race, User user) {
        String jpql = "";
        jpql += " select ";
        jpql += "    new UserRegistration( ";
        jpql += "        u.id, ";
        jpql += "        r.id, ";
        jpql += "        r.teamName ";
        jpql += "        ) ";
        jpql += "   from UserRegistration tf ";
        jpql += "   join tf.user u ";
        jpql += "   join tf.registration r ";
        jpql += "   join r.raceCategory rc ";
        jpql += "   join rc.race ra ";
        jpql += "  where ra = :race ";
        jpql += "    and u = :user ";
        jpql += "    and r.status <> :status ";

        TypedQuery<UserRegistration> query = em.createQuery(jpql, UserRegistration.class);
        query.setParameter("race", race);
        query.setParameter("user", user);
        query.setParameter("status", CANCELLED);

        UserRegistration result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }
}
