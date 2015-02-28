//package adventure.rest;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//
//import adventure.entity.GenderType;
//import adventure.entity.Race;
//import adventure.entity.RaceOrganizer;
//import adventure.entity.User;
//import adventure.persistence.RaceDAO;
//import adventure.persistence.RaceOrganizerDAO;
//import adventure.persistence.UserDAO;
//import adventure.rest.RaceREST.OrganizerData;
//import br.gov.frameworkdemoiselle.ForbiddenException;
//import br.gov.frameworkdemoiselle.NotFoundException;
//import br.gov.frameworkdemoiselle.UnprocessableEntityException;
//import br.gov.frameworkdemoiselle.security.LoggedIn;
//
//@Path("race/{id}/organizers")
//public class RaceOrganizerREST {
//
//	@GET
//	@Produces("application/json")
//	public List<OrganizerData> list(@PathParam("id") Integer id) throws Exception {
//		Race race = loadRace(id);
//		List<OrganizerData> result = new ArrayList<OrganizerData>();
//
//		for (User organizer : UserDAO.getInstance().findRaceOrganizers(race)) {
//			OrganizerData data = new OrganizerData();
//			data.id = organizer.getId();
//			data.name = organizer.getProfile().getName();
//			data.email = organizer.getEmail();
//			result.add(data);
//		}
//
//		return result.isEmpty() ? null : result;
//	}
//
//	@POST
//	@LoggedIn
//	@Consumes("application/json")
//	public void add(@PathParam("id") Integer id, List<Integer> ids) throws Exception {
//		Race race = loadRace(id);
//		checkPermission(race);
//
//		if (ids == null || ids.isEmpty()) {
//			throw new UnprocessableEntityException().addViolation("ids", "O parâmetro \"ids\" é obrigatório.");
//		}
//
//		UnprocessableEntityException exception = new UnprocessableEntityException();
//		UserDAO userDAO = UserDAO.getInstance();
//		RaceOrganizerDAO raceOrganizerDAO = RaceOrganizerDAO.getInstance();
//		for (Integer organizerId : ids) {
//			User user = userDAO.loadBasics(organizerId);
//			if (user == null) {
//				exception.addViolation("Usuário com id " + organizerId + " não encontrado.");
//			}
//
//			RaceOrganizer persisted = null;
//			if (user != null) {
//				persisted = raceOrganizerDAO.load(race, user);
//				if (persisted != null) {
//					exception.addViolation(user.getProfile().getName() + " já é organizador"
//							+ (user.getProfile().getGender() == GenderType.FEMALE ? "a" : ""));
//				}
//			}
//
//			if (persisted == null) {
//				RaceOrganizer raceOrganizer = new RaceOrganizer(race, userDAO.load(organizerId));
//				raceOrganizerDAO.insert(raceOrganizer);
//			}
//		}
//
//		if (!exception.getViolations().isEmpty()) {
//			throw exception;
//		}
//	}
//
//	@DELETE
//	@LoggedIn
//	public void delete(@PathParam("id") Integer id, List<Integer> ids) throws Exception {
//		Race race = loadRace(id);
//		checkPermission(race);
//
//		if (ids == null || ids.isEmpty()) {
//			throw new UnprocessableEntityException().addViolation("ids", "O parâmetro \"ids\" é obrigatório.");
//		}
//
//		UnprocessableEntityException exception = new UnprocessableEntityException();
//		UserDAO userDAO = UserDAO.getInstance();
//		RaceOrganizerDAO raceOrganizerDAO = RaceOrganizerDAO.getInstance();
//		for (Integer organizerId : ids) {
//			User user = userDAO.loadBasics(organizerId);
//			if (user == null) {
//				exception.addViolation("Usuário com id " + organizerId + " não encontrado.");
//			}
//
//			RaceOrganizer persisted = null;
//			if (user != null) {
//				persisted = raceOrganizerDAO.load(race, user);
//				if (persisted == null) {
//					exception.addViolation(user.getProfile().getName() + " não é organizador"
//							+ (user.getProfile().getGender() == GenderType.FEMALE ? "a" : ""));
//				}
//			}
//
//			if (persisted != null) {
//				raceOrganizerDAO.delete(persisted);
//			}
//		}
//
//		if (!exception.getViolations().isEmpty()) {
//			throw exception;
//		}
//	}
//
//	private Race loadRace(Integer id) throws Exception {
//		Race result = RaceDAO.getInstance().load(id);
//
//		if (result == null) {
//			throw new NotFoundException();
//		}
//
//		return result;
//	}
//
//	private void checkPermission(Race race) throws ForbiddenException {
//		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
//		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
//			throw new ForbiddenException();
//		}
//	}
//
// }
