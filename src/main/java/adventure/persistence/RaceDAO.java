package adventure.persistence;

import static javax.persistence.TemporalType.DATE;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Race;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class RaceDAO extends JPACrud<Race, Long> {

	private static final long serialVersionUID = 1L;

	public List<Race> findNext() throws Exception {
		String jpql = "from Race where date >= :date order by date";

		TypedQuery<Race> query = getEntityManager().createQuery(jpql, Race.class);
		query.setParameter("date", new Date(), DATE);

		return query.getResultList();
	}
}
