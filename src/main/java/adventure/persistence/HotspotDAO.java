package adventure.persistence;

import java.util.List;

import javax.persistence.TypedQuery;

import adventure.entity.Event;
import adventure.entity.Hotspot;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Transactional
public class HotspotDAO extends JPACrud<Hotspot, Integer> {

	private static final long serialVersionUID = 1L;

	public static HotspotDAO getInstance() {
		return Beans.getReference(HotspotDAO.class);
	}

	public List<Hotspot> find(Event event) {
		StringBuffer jpql = new StringBuffer();
		jpql.append("select ");
		jpql.append("   new Hotspot ( ");
		jpql.append("       h.id, ");
		jpql.append("       h.name, ");
		jpql.append("       h.description, ");
		jpql.append("       h.coord, ");
		jpql.append("       h.order, ");
		jpql.append("       h.main, ");
		jpql.append("       e.id ");
		jpql.append("       ) ");
		jpql.append("  from Hotspot h ");
		jpql.append("  join h.event e ");
		jpql.append(" where e = :event ");
		jpql.append(" order by ");
		jpql.append(" 		h.order asc ");

		TypedQuery<Hotspot> query = getEntityManager().createQuery(jpql.toString(), Hotspot.class);
		query.setParameter("event", event);

		return query.getResultList();
	}
}
