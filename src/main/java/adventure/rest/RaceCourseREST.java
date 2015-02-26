package adventure.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Course;
import adventure.entity.Race;
import adventure.entity.User;
import adventure.persistence.CourseDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.UserDAO;
import adventure.rest.RaceREST.CourseData;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.NotFoundException;

@Path("race/{id}/courses")
public class RaceCourseREST {

	@GET
	@Produces("application/json")
	public List<CourseData> list(@PathParam("id") Integer id) throws Exception {
		Race race = loadRace(id);
		List<CourseData> result = new ArrayList<CourseData>();

		for (Course course : CourseDAO.getInstance().findWithCategories(race)) {
			CourseData data = new CourseData();
			data.id = course.getId();
			data.length = course.getLength();

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	// @PUT
	// @LoggedIn
	// @ValidatePayload
	// @Path("{length}")
	// @Consumes("application/json")
	// public void insert(@PathParam("id") Integer id, @PathParam("length") Integer length) throws Exception {
	// Race race = loadRace(id);
	// checkPermission(race);
	//
	// CourseDAO courseDAO = CourseDAO.getInstance();
	// Course persisted = courseDAO.load(race, length);
	//
	// if (persisted == null) {
	// persisted = new Course();
	// persisted.setRace(race);
	// persisted.setLength(length);
	//
	// courseDAO.insert(persisted);
	// }
	// }

	// @POST
	// @LoggedIn
	// @Consumes("application/json")
	// public void add(@PathParam("id") Integer id, List<Integer> ids) throws Exception {
	// Race race = loadRace(id);
	// checkPermission(race);
	//
	// if (ids == null || ids.isEmpty()) {
	// throw new UnprocessableEntityException().addViolation("ids", "O parâmetro \"ids\" é obrigatório.");
	// }
	//
	// UnprocessableEntityException exception = new UnprocessableEntityException();
	// UserDAO userDAO = UserDAO.getInstance();
	// RaceOrganizerDAO raceOrganizerDAO = RaceOrganizerDAO.getInstance();
	// for (Integer organizerId : ids) {
	// User user = userDAO.loadBasics(organizerId);
	// if (user == null) {
	// exception.addViolation("Usuário com id " + organizerId + " não encontrado.");
	// }
	//
	// RaceOrganizer persisted = null;
	// if (user != null) {
	// persisted = raceOrganizerDAO.load(race, user);
	// if (persisted != null) {
	// exception.addViolation(user.getProfile().getName() + " já é organizador"
	// + (user.getProfile().getGender() == GenderType.FEMALE ? "a" : ""));
	// }
	// }
	//
	// if (persisted == null) {
	// RaceOrganizer raceOrganizer = new RaceOrganizer(race, userDAO.load(organizerId));
	// raceOrganizerDAO.insert(raceOrganizer);
	// }
	// }
	//
	// if (!exception.getViolations().isEmpty()) {
	// throw exception;
	// }
	// }
	//
	// @DELETE
	// @LoggedIn
	// public void delete(@PathParam("id") Integer id, List<Integer> ids) throws Exception {
	// Race race = loadRace(id);
	// checkPermission(race);
	//
	// if (ids == null || ids.isEmpty()) {
	// throw new UnprocessableEntityException().addViolation("ids", "O parâmetro \"ids\" é obrigatório.");
	// }
	//
	// UnprocessableEntityException exception = new UnprocessableEntityException();
	// UserDAO userDAO = UserDAO.getInstance();
	// RaceOrganizerDAO raceOrganizerDAO = RaceOrganizerDAO.getInstance();
	// for (Integer organizerId : ids) {
	// User user = userDAO.loadBasics(organizerId);
	// if (user == null) {
	// exception.addViolation("Usuário com id " + organizerId + " não encontrado.");
	// }
	//
	// RaceOrganizer persisted = null;
	// if (user != null) {
	// persisted = raceOrganizerDAO.load(race, user);
	// if (persisted == null) {
	// exception.addViolation(user.getProfile().getName() + " não é organizador"
	// + (user.getProfile().getGender() == GenderType.FEMALE ? "a" : ""));
	// }
	// }
	//
	// if (persisted != null) {
	// raceOrganizerDAO.delete(persisted);
	// }
	// }
	//
	// if (!exception.getViolations().isEmpty()) {
	// throw exception;
	// }
	// }

	private Race loadRace(Integer id) throws Exception {
		Race result = RaceDAO.getInstance().load(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private void checkPermission(Race race) throws ForbiddenException {
		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}
	}

	public static class CourseDataInsert {

		public Integer length;
	}
}
