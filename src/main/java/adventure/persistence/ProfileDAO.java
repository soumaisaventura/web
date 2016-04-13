package adventure.persistence;

import adventure.entity.Profile;
import adventure.entity.User;
import adventure.util.Misc;
import adventure.util.PendencyCounter;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Transactional
public class ProfileDAO extends JPACrud<Profile, User> {

    private static final long serialVersionUID = 1L;

    public static ProfileDAO getInstance() {
        return Beans.getReference(ProfileDAO.class);
    }

    @Override
    public void delete(User id) {
        getEntityManager().remove(load(id));
    }

    @Override
    public Profile insert(Profile profile) {
        if (profile.getName() != null) {
            profile.setName(Misc.capitalize(profile.getName()));
        }

        profile.setPendencies(PendencyCounter.count(profile));
        return super.insert(profile);
    }

    @Override
    public Profile update(Profile profile) {
        if (profile.getName() != null) {
            profile.setName(Misc.capitalize(profile.getName()));
        }

        profile.setPendencies(PendencyCounter.count(profile));
        return super.update(profile);
    }

    public Profile loadDetails(Integer id) {
        User user = new User();
        user.setId(id);
        return loadDetails(user);
    }

    public Profile loadDetails(User user) {
        String jpql = "";
        jpql += " select ";
        jpql += " 	 new Profile( ";
        jpql += " 	     p.name, ";
        jpql += " 	     p.rg, ";
        jpql += " 	     p.cpf, ";
        jpql += " 	     p.birthday, ";
        jpql += " 	     p.mobile, ";
        jpql += " 	     p.gender, ";
        jpql += " 	     p.tshirt, ";
        jpql += " 	     p.pendencies, ";
        jpql += " 	     u.id, ";
        jpql += " 	     u.email, ";
        jpql += " 	     c.id, ";
        jpql += " 	     c.name, ";
        jpql += " 	     s.id, ";
        jpql += " 	     s.name, ";
        jpql += " 	     s.abbreviation ";
        jpql += " 	 ) ";
        jpql += "   from Profile p ";
        jpql += "   join p.user u ";
        jpql += "   left join p.city c ";
        jpql += "   left join c.state s ";
        jpql += "  where p.user = :user ";

        TypedQuery<Profile> query = getEntityManager().createQuery(jpql, Profile.class);
        query.setParameter("user", user);

        Profile result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    @Override
    public Profile load(User user) {
        String jpql = "from Profile p where p.user = :user";
        TypedQuery<Profile> query = getEntityManager().createQuery(jpql, Profile.class);
        query.setParameter("user", user);

        Profile result;
        try {
            result = query.getSingleResult();
        } catch (NoResultException cause) {
            result = null;
        }
        return result;
    }

    public Profile load(Integer id) {
        User user = new User();
        user.setId(id);
        return load(user);
    }
}
