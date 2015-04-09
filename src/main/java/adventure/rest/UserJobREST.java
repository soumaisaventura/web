package adventure.rest;

import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import adventure.entity.Profile;
import adventure.entity.User;
import adventure.persistence.ProfileDAO;
import adventure.persistence.UserDAO;
import adventure.util.Misc;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.NameQualifier;

@Path("user")
public class UserJobREST {

	private transient Logger logger;

	@POST
	@Transactional
	@Path("profile/capitalize")
	public void capitalize() throws Exception {
		getLogger().info("Buscando por nomes de usuários para capitalizar");
		ProfileDAO profileDAO = ProfileDAO.getInstance();

		for (Profile profile : profileDAO.findAll()) {
			String adjusted = Misc.capitalize(profile.getName());

			if (!profile.getName().equals(adjusted)) {
				getLogger().info("Ajustando o nome [" + profile.getName() + "] para [" + adjusted + "]");

				profile.setName(adjusted);
				profileDAO.update(profile);
			}
		}

		getLogger().info("Buscando por e-mails para ajustar");
		UserDAO userDAO = UserDAO.getInstance();

		for (User user : userDAO.findAll()) {
			String adjusted = user.getEmail().trim().toLowerCase();

			if (!user.getEmail().equals(adjusted)) {
				getLogger().info("Ajustando o e-mail [" + user.getEmail() + "] para [" + adjusted + "]");

				user.setEmail(adjusted);
				userDAO.update(user);
			}
		}
	}

	private Logger getLogger() {
		if (this.logger == null) {
			this.logger = Beans.getReference(Logger.class, new NameQualifier(UserJobREST.class.getName()));
		}

		return this.logger;
	}
}
