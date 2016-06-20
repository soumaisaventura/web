package adventure.persistence;

import adventure.entity.Race;
import adventure.entity.RegistrationPeriod;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

@Transactional
public class PeriodDAO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public static PeriodDAO getInstance() {
        return Beans.getReference(PeriodDAO.class);
    }

    public List<RegistrationPeriod> findForEvent(Race race) {
        String jpql = "";
        jpql += " select p ";
        jpql += "   from RegistrationPeriod p ";
        jpql += "  where p.race = :race ";
        jpql += "  order by ";
        jpql += "        p.beginning ";

        TypedQuery<RegistrationPeriod> query = em.createQuery(jpql, RegistrationPeriod.class);
        query.setParameter("race", race);

        return query.getResultList();
    }

    // TODO: OLD

    public RegistrationPeriod load(Race race, Date date) throws Exception {
        String jpql = "";
        jpql += " select p ";
        jpql += "   from RegistrationPeriod p ";
        jpql += "   join p.race r ";
        jpql += "  where r = :race ";
        jpql += " 	 and :date between p.beginning and p.end ";
        jpql += "  order by ";
        jpql += "        p.beginning ";

        TypedQuery<RegistrationPeriod> query = em.createQuery(jpql, RegistrationPeriod.class);
        query.setParameter("race", race);
        query.setParameter("date", date, DATE);

        RegistrationPeriod result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    // public List<RegistrationPeriod> find(Race race) throws Exception {
    // String jpql = "";
    // jpql += " select p ";
    // jpql += "   from RegistrationPeriod p ";
    // jpql += "   join p.race r ";
    // jpql += "  where r = :race ";
    // jpql += "  order by ";
    // jpql += "        p.beginning ";
    //
    // TypedQuery<RegistrationPeriod> query = em.createQuery(jpql, RegistrationPeriod.class);
    // query.setParameter("race", race);
    //
    // return query.getResultList();
    // }
}
