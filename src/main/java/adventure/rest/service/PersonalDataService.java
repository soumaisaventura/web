package adventure.rest.service;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import adventure.entity.PersonalData;
import adventure.persistence.PersonalDataDAO;

@Path("/api/personaldata")
public class PersonalDataService {

	@Inject
	PersonalDataDAO dao;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void insert(PersonalData personalData) throws Exception {
		dao.insert(personalData);
	}

}
