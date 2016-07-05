package adventure.business;

import adventure.entity.Kit;
import adventure.entity.Race;
import adventure.entity.User;
import adventure.persistence.KitDAO;
import adventure.persistence.UserDAO;
import adventure.rest.data.UserData;
import adventure.security.Passwords;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class UserBusiness {

    public static UserBusiness getInstance() {
        return Beans.getReference(UserBusiness.class);
    }

    @Transactional
    public String updateActivationToken(String email) {
        UserDAO userDAO = UserDAO.getInstance();
        User user = userDAO.load(email);
        String token;

        if (user.getActivationToken() == null) {
            token = Passwords.randomToken();
            user.setActivationToken(token);
            userDAO.update(user);
        } else {
            token = user.getActivationToken();
        }


        return token;
    }

    @Transactional
    public String updateResetToken(String email) {
        UserDAO userDAO = UserDAO.getInstance();
        User user = userDAO.load(email);
        String token;

        if (user.getPasswordResetToken() == null) {
            token = Passwords.randomToken();
            user.setPasswordResetToken(token);
            user.setPasswordResetRequest(new Date());
            userDAO.update(user);
        } else {
            token = user.getPasswordResetToken();
        }

        return token;
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
                exception.addViolation("Usuário " + user.getName() + " duplicado.");
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

    public BigDecimal getKitPrice(User user) {
        return user.getKit() == null ? ZERO : user.getKit().getPrice();
    }
}
