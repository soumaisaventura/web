package adventure.rest;

import adventure.business.MailBusiness;
import adventure.entity.Registration;
import adventure.entity.RegistrationPeriod;
import adventure.entity.UserRegistration;
import adventure.persistence.PeriodDAO;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.UserRegistrationDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.NameQualifier;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

@Path("registration")
public class RegistrationJobREST {

    private transient Logger logger;

    @POST
    @Transactional
    @Path("period/update")
    public void updatePeriod(@Context UriInfo uriInfo) throws Exception {
        getLogger().info("Iniciando a busca por inscrições expiradas...");

        RegistrationDAO registrationDAO = RegistrationDAO.getInstance();
        List<Registration> expired = registrationDAO.findForUpdatePeriod();
        UserRegistrationDAO userRegistrationDAO = UserRegistrationDAO.getInstance();
        PeriodDAO periodDAO = PeriodDAO.getInstance();
        MailBusiness mailBusiness = MailBusiness.getInstance();

        for (Registration registration : expired) {
            RegistrationPeriod period = periodDAO.loadCurrent(registration.getRaceCategory().getRace());

            for (UserRegistration teamFormation : userRegistrationDAO.find(registration)) {
                UserRegistration persisted = userRegistrationDAO.load(registration, teamFormation.getUser());
                persisted.setAmount(period.getPrice());
                userRegistrationDAO.update(persisted);
            }

            Registration persisted = registrationDAO.load(registration.getId());
            persisted.setPeriod(period);

            if (persisted.getPayment() != null) {
                persisted.getPayment().setCode(null);
            }

            registrationDAO.update(persisted);
            getLogger().info("Os valores da inscrição #" + registration.getFormattedId() + " foram atualizados.");

            URI baseUri = uriInfo.getBaseUri().resolve("..");
            mailBusiness.sendRegistrationPeriodChanging(registration, period.getBeginning(), period.getEnd(), baseUri);
        }
    }

    private Logger getLogger() {
        if (this.logger == null) {
            this.logger = Beans.getReference(Logger.class, new NameQualifier(RegistrationJobREST.class.getName()));
        }

        return this.logger;
    }
}
