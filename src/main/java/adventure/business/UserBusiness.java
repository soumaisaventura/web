package adventure.business;

import adventure.entity.Kit;
import adventure.entity.Race;
import adventure.entity.User;
import adventure.persistence.KitDAO;
import adventure.persistence.UserDAO;
import adventure.rest.data.UserData;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Beans;

import java.util.ArrayList;
import java.util.List;

public class UserBusiness {

    public static UserBusiness getInstance() {
        return Beans.getReference(UserBusiness.class);
    }

    public List<User> loadMembers(Race race, List<UserData> members) throws Exception {
        UserDAO userDAO = UserDAO.getInstance();
        KitDAO kitDAO = KitDAO.getInstance();

        List<User> result = new ArrayList();
        UnprocessableEntityException exception = new UnprocessableEntityException();

        for (UserData member : members) {
            User user = userDAO.loadBasics(member.id);
            Kit kit = null;

            if (user == null) {
                exception.addViolation("Usuário " + member.id + " inválido.");
            } else if (result.contains(user)) {
                exception.addViolation("Usuário " + member.id + " duplicado.");
            } else {
                if (member.kit != null) {
                    kit = kitDAO.loadForRegistration(race, member.kit.id);

                    if (kit == null) {
                        exception.addViolation("Kit " + member.kit.id + " inválido.");
                    }
                }

                user.setKit(kit);
                result.add(user);
            }
        }

        if (!exception.getViolations().isEmpty()) {
            throw exception;
        }

        return result;
    }
}
