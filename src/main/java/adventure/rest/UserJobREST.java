package adventure.rest;

import java.util.logging.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import adventure.entity.Profile;
import adventure.persistence.ProfileDAO;
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
			String capitalized = Misc.capitalize(profile.getName());

			if (!profile.getName().equals(capitalized)) {
				getLogger().info("Ajustando o nome [" + profile.getName() + "] para [" + capitalized + "]");

				profile.setName(capitalized);
				profileDAO.update(profile);
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
