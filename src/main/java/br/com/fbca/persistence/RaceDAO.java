package br.com.fbca.persistence;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import br.com.fbca.entity.Race;
import br.com.fbca.util.Dates;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class RaceDAO extends JPACrud<Race, Long> {

	private static final long serialVersionUID = 1L;

	public List<Race> findNext() throws Exception {
		String jpql = "from Race where date >= :date order by date";

		TypedQuery<Race> query = getEntityManager().createQuery(jpql, Race.class);
		query.setParameter("date", Dates.ignoreTime(new Date()));

		return query.getResultList();
	}
}
