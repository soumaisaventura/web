package adventure.rest;

import adventure.business.MailBusiness;
import adventure.entity.User;
import adventure.persistence.HealthDAO;
import adventure.persistence.ProfileDAO;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.transaction.TransactionContext;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.NameQualifier;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

@Path("user")
public class UserJobREST {

    private transient Logger logger;

    @POST
    @Path("duplicates/removal")
    @Produces("application/json")
    public List<User> remove() throws Exception {
        List<User> result = new ArrayList();

        ProfileDAO profileDAO = ProfileDAO.getInstance();
        HealthDAO healthDAO = HealthDAO.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        MailBusiness mailBusiness = MailBusiness.getInstance();

        TransactionContext context = Beans.getReference(TransactionContext.class);
        User previous = null;

        List<String> dups = new ArrayList();

        for (User user : userDAO.findDuplicatesByName()) {
            if (previous == null || !user.getProfile().getName().equals(previous.getProfile().getName())) {
                context.getCurrentTransaction().begin();
            }

            try {
                if (context.getCurrentTransaction().isActive()) {
                    List<User> persistedByName = userDAO.findByName(user.getProfile().getName());
                    if (persistedByName.size() > 1) {
                        User persisted = userDAO.load(user.getId());
                        healthDAO.delete(persisted);
                        profileDAO.delete(persisted);
                        userDAO.delete(persisted.getId());
                        result.add(user);
                        dups.add(user.getEmail());

                        getLogger().info(
                                "Excluindo duplicata " + user.getProfile().getName() + " com o e-mail "
                                        + user.getEmail());
                    }

                    persistedByName = userDAO.findByName(user.getProfile().getName());
                    if (persistedByName.size() == 1) {
                        User original = persistedByName.get(0);

                        for (String email : dups) {
                            mailBusiness.sendAccountRemoval(original.getEmail(), email);
                        }

                        getLogger().info(
                                "Mantendo " + original.getProfile().getName() + " com o e-mail " + original.getEmail());
                        context.getCurrentTransaction().commit();
                        dups.clear();
                    }

                }

            } catch (Exception cause) {
                getLogger().log(
                        SEVERE,
                        "Falha ao tratar a duplicata " + user.getProfile().getName() + " com o e-mail "
                                + user.getEmail(), cause);
                context.getCurrentTransaction().rollback();
            }

            previous = user;
        }

        return result.isEmpty() ? null : result;
    }

    private Logger getLogger() {
        if (this.logger == null) {
            this.logger = Beans.getReference(Logger.class, new NameQualifier(UserJobREST.class.getName()));
        }

        return this.logger;
    }
}
