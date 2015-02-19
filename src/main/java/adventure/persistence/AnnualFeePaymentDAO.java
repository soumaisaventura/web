package adventure.persistence;

import static java.util.Calendar.YEAR;

import java.io.Serializable;
import java.util.Calendar;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.AnnualFeePayment;
import adventure.entity.User;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class AnnualFeePaymentDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static AnnualFeePaymentDAO getInstance() {
		return Beans.getReference(AnnualFeePaymentDAO.class);
	}

	@Transactional
	public AnnualFeePayment insert(AnnualFeePayment entity) {
		em.persist(entity);
		return entity;
	}

	public AnnualFeePayment loadCurrent(User user) {
		return load(user, Calendar.getInstance().get(YEAR));
	}

	public AnnualFeePayment load(User user, Integer year) {
		StringBuffer jpql = new StringBuffer();
		jpql.append(" select afp ");
		jpql.append("   from AnnualFeePayment afp ");
		jpql.append("   join afp.annualFee af ");
		jpql.append("  where af.year = :year ");
		jpql.append("    and afp.user = :user ");

		TypedQuery<AnnualFeePayment> query = em.createQuery(jpql.toString(), AnnualFeePayment.class);
		query.setParameter("user", user);
		query.setParameter("year", year);

		AnnualFeePayment result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}
}
