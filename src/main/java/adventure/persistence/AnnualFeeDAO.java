package adventure.persistence;

import static java.util.Calendar.YEAR;

import java.io.Serializable;
import java.util.Calendar;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import adventure.entity.AnnualFee;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class AnnualFeeDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public static AnnualFeeDAO getInstance() {
		return Beans.getReference(AnnualFeeDAO.class);
	}

	public AnnualFee loadCurrent() {
		return load(Calendar.getInstance().get(YEAR));
	}

	public AnnualFee load(Integer year) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("  from AnnualFee af ");
		jpql.append(" where af.year = :year ");

		TypedQuery<AnnualFee> query = em.createQuery(jpql.toString(), AnnualFee.class);
		query.setParameter("year", year);

		AnnualFee result;
		try {
			result = query.getSingleResult();
		} catch (NoResultException cause) {
			result = null;
		}
		return result;
	}
}
