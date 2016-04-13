package adventure.persistence;

import adventure.entity.Event;
import adventure.entity.Hotspot;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class HotspotDAO extends JPACrud<Hotspot, Integer> {

    private static final long serialVersionUID = 1L;

    public static HotspotDAO getInstance() {
        return Beans.getReference(HotspotDAO.class);
    }

    public List<Hotspot> find(Event event) {
        String jpql = "";
        jpql += "select ";
        jpql += "   new Hotspot ( ";
        jpql += "       h.id, ";
        jpql += "       h.name, ";
        jpql += "       h.description, ";
        jpql += "       h.coord, ";
        jpql += "       h.order, ";
        jpql += "       h.main, ";
        jpql += "       e.id ";
        jpql += "       ) ";
        jpql += "  from Hotspot h ";
        jpql += "  join h.event e ";
        jpql += " where e = :event ";
        jpql += " order by ";
        jpql += " 		h.order asc ";

        TypedQuery<Hotspot> query = getEntityManager().createQuery(jpql, Hotspot.class);
        query.setParameter("event", event);

        return query.getResultList();
    }
}
