package adventure.rest;

import static adventure.entity.RegistrationStatusType.CANCELLED;
import static adventure.entity.RegistrationStatusType.CONFIRMED;
import static adventure.entity.RegistrationStatusType.PENDENT;
import static adventure.entity.Status.OPEN_ID;
import static java.util.Locale.US;

import java.math.BigDecimal;
import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import adventure.business.MailBusiness;
import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.RegistrationPayment;
import adventure.entity.User;
import adventure.entity.UserRegistration;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.UserDAO;
import adventure.persistence.UserRegistrationDAO;
import adventure.rest.data.CategoryData;
import adventure.rest.data.CityData;
import adventure.rest.data.CoordsData;
import adventure.rest.data.EventData;
import adventure.rest.data.EventPaymentData;
import adventure.rest.data.LocationData;
import adventure.rest.data.PeriodData;
import adventure.rest.data.RaceData;
import adventure.rest.data.RegistrationData;
import adventure.rest.data.RegistrationPaymentData;
import adventure.rest.data.TeamData;
import adventure.rest.data.UserData;
import br.gov.frameworkdemoiselle.ForbiddenException;
import br.gov.frameworkdemoiselle.HttpViolationException;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.security.LoggedIn;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.NameQualifier;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("registration")
public class RegistrationREST {

	public static RegistrationREST getInstance() {
		return Beans.getReference(RegistrationREST.class);
	}

	@GET
	@LoggedIn
	@Transactional
	@Produces("application/json")
	public List<RegistrationData> find() throws Exception {
		List<RegistrationData> result = new ArrayList<RegistrationData>();
		User loggedInUser = User.getLoggedIn();

		for (Registration registration : RegistrationDAO.getInstance().find(loggedInUser)) {
			RegistrationData data = new RegistrationData();
			data.id = registration.getId();
			data.number = registration.getFormattedId();
			data.team = new TeamData();
			data.team.name = registration.getTeamName();
			data.status = registration.getStatus();

			data.race = new RaceData();
			data.race.internalId = registration.getRaceCategory().getRace().getId();
			data.race.id = registration.getRaceCategory().getRace().getSlug();
			data.race.name = registration.getRaceCategory().getRace().getName();

			data.race.period = new PeriodData();
			data.race.period.beginning = registration.getRaceCategory().getRace().getPeriod().getBeginning();
			data.race.period.end = registration.getRaceCategory().getRace().getPeriod().getEnd();

			data.race.event = new EventData();
			data.race.event.internalId = registration.getRaceCategory().getRace().getEvent().getId();
			data.race.event.id = registration.getRaceCategory().getRace().getEvent().getSlug();
			data.race.event.name = registration.getRaceCategory().getRace().getEvent().getName();

			data.race.event.location = new LocationData();
			data.race.event.location.city = new CityData();
			data.race.event.location.city.id = registration.getRaceCategory().getRace().getEvent().getCity().getId();
			data.race.event.location.city.name = registration.getRaceCategory().getRace().getEvent().getCity()
					.getName();
			data.race.event.location.city.state = registration.getRaceCategory().getRace().getEvent().getCity()
					.getState().getAbbreviation();

			result.add(data);
		}

		return result.isEmpty() ? null : result;
	}

	@POST
	@LoggedIn
	@Transactional
	@Path("{id}/confirm")
	public void confirm(@PathParam("id") Long id, @Context UriInfo uriInfo) throws Exception {
		Registration registration = loadRegistrationForDetails(id);

		List<User> organizers = UserDAO.getInstance().findOrganizers(
				registration.getRaceCategory().getRace().getEvent());
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}

		if (registration.getStatus() != PENDENT) {
			throw new UnprocessableEntityException().addViolation("Só é possível confirmar inscrições pendentes.");
		}

		registration.setStatus(CONFIRMED);
		registration.setStatusDate(new Date());
		registration.setApprover(User.getLoggedIn());
		RegistrationDAO.getInstance().update(registration);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailBusiness.getInstance().sendRegistrationConfirmation(registration, baseUri);
	}

	@POST
	@LoggedIn
	@Transactional
	@Path("{id}/cancel")
	public void cancel(@PathParam("id") Long id, @Context UriInfo uriInfo) throws Exception {
		Registration registration = loadRegistrationForDetails(id);

		List<User> organizers = UserDAO.getInstance().findOrganizers(
				registration.getRaceCategory().getRace().getEvent());
		if (!User.getLoggedIn().getAdmin() && !organizers.contains(User.getLoggedIn())) {
			throw new ForbiddenException();
		}

		switch (registration.getStatus()) {
			case CANCELLED:
				throw new UnprocessableEntityException().addViolation("Esta inscrição já foi cancelada.");

			case CONFIRMED:
				if (!User.getLoggedIn().getAdmin()) {
					throw new ForbiddenException()
							.addViolation("Somente os administradores podem cancelar uma inscrição já confirmada.");
				}
				break;

			default:
				break;
		}

		registration.setStatus(CANCELLED);
		registration.setStatusDate(new Date());
		registration.setApprover(User.getLoggedIn());
		RegistrationDAO.getInstance().update(registration);

		URI baseUri = uriInfo.getBaseUri().resolve("..");
		MailBusiness.getInstance().sendRegistrationCancellation(registration, baseUri);
	}

	@GET
	@LoggedIn
	@Path("{id}")
	@Transactional
	@Produces("application/json")
	public RegistrationData load(@PathParam("id") Long id) throws Exception {
		Registration registration = loadRegistrationForDetails(id);

		RegistrationData data = new RegistrationData();
		data.id = registration.getId();
		data.number = registration.getFormattedId();
		data.date = registration.getDate();
		data.status = registration.getStatus();

		data.payment = new RegistrationPaymentData();
		data.payment.code = registration.getPayment().getCode();
		data.payment.transaction = registration.getPayment().getTransaction();

		data.submitter = new UserData();
		data.submitter.id = registration.getSubmitter().getId();
		data.submitter.email = registration.getSubmitter().getEmail();
		data.submitter.name = registration.getSubmitter().getProfile().getName();
		data.submitter.mobile = registration.getSubmitter().getProfile().getMobile();

		data.team = new TeamData();
		data.team.name = registration.getTeamName();

		data.race = new RaceData();
		data.race.id = registration.getRaceCategory().getRace().getSlug();
		data.race.internalId = registration.getRaceCategory().getRace().getId();
		data.race.name = registration.getRaceCategory().getRace().getName();
		data.race.distance = registration.getRaceCategory().getRace().getDistance();
		data.race.status = registration.getRaceCategory().getRace().getStatus().getName();

		data.race.period = new PeriodData();
		data.race.period.beginning = registration.getRaceCategory().getRace().getPeriod().getBeginning();
		data.race.period.end = registration.getRaceCategory().getRace().getPeriod().getEnd();

		data.race.event = new EventData();
		data.race.event.id = registration.getRaceCategory().getRace().getEvent().getSlug();
		data.race.event.internalId = registration.getRaceCategory().getRace().getEvent().getId();
		data.race.event.name = registration.getRaceCategory().getRace().getEvent().getName();

		data.race.event.payment = new EventPaymentData();
		data.race.event.payment.type = registration.getRaceCategory().getRace().getEvent().getPayment().getType();
		data.race.event.payment.info = registration.getRaceCategory().getRace().getEvent().getPayment().getInfo();

		data.race.event.location = new LocationData();
		data.race.event.location.coords = new CoordsData();
		data.race.event.location.coords.latitude = registration.getRaceCategory().getRace().getEvent().getCoords()
				.getLatitude();
		data.race.event.location.coords.longitude = registration.getRaceCategory().getRace().getEvent().getCoords()
				.getLongitude();

		data.race.event.location.city = new CityData();
		data.race.event.location.city.id = registration.getRaceCategory().getRace().getEvent().getCity().getId();
		data.race.event.location.city.name = registration.getRaceCategory().getRace().getEvent().getCity().getName();
		data.race.event.location.city.state = registration.getRaceCategory().getRace().getEvent().getCity().getState()
				.getAbbreviation();

		data.category = new CategoryData();
		data.category.name = registration.getRaceCategory().getCategory().getName();

		// Race race = registration.getRaceCategory().getRace();
		// List<User> organizers = UserDAO.getInstance().findOrganizersForEvent(race.getEvent());
		// User loggedInUser = User.getLoggedIn();
		//
		// if (!loggedInUser.getAdmin() && !registration.getSubmitter().equals(loggedInUser)
		// && !userRegistrations.contains(new UserRegistration(registration, loggedInUser))
		// /* && !organizers.contains(loggedInUser) */) {
		// throw new ForbiddenException();
		// }

		data.race.event.organizers = new ArrayList<UserData>();
		for (User organizer : UserDAO.getInstance().findOrganizers(registration.getRaceCategory().getRace().getEvent())) {
			UserData userData = new UserData();
			userData.id = organizer.getId();
			userData.email = organizer.getEmail();
			userData.name = organizer.getProfile().getName();
			userData.mobile = organizer.getProfile().getMobile();
			data.race.event.organizers.add(userData);
		}

		data.team.members = new ArrayList<UserData>();
		for (UserRegistration userRegistration : UserRegistrationDAO.getInstance().find(registration)) {
			UserData userData = new UserData();
			userData.id = userRegistration.getUser().getId();
			userData.email = userRegistration.getUser().getEmail();
			userData.name = userRegistration.getUser().getProfile().getName();
			userData.mobile = userRegistration.getUser().getProfile().getMobile();
			userData.racePrice = userRegistration.getRacePrice();
			data.team.members.add(userData);
		}

		return data;
	}

	@POST
	@LoggedIn
	@Transactional
	@Path("{id}/payment")
	@Produces("text/plain")
	public Response sendPayment(@PathParam("id") Long id, @Context UriInfo uriInfo) throws Exception {
		Registration registration = loadRegistrationForDetails(id);
		String code = registration.getPayment().getCode();
		int status;

		if (code == null) {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			code = createCode(registration, baseUri);

			Registration persistedRegistration = RegistrationDAO.getInstance().load(registration.getId());
			persistedRegistration.setPayment(new RegistrationPayment());
			persistedRegistration.getPayment().setCode(code);
			RegistrationDAO.getInstance().update(persistedRegistration);

			status = 201;
		} else {
			status = 200;
		}

		URI location = URI.create("https://pagseguro.uol.com.br/v2/checkout/payment.html?code=" + code);
		return Response.status(status).location(location).entity(code).build();
	}

	@PUT
	@LoggedIn
	@Transactional
	@Produces("application/json")
	@Path("{id}/member/{memberId}/price")
	public void updateRacePrice(@PathParam("id") Long id, @PathParam("memberId") Integer memberId, BigDecimal price)
			throws Exception {
		Registration registration = loadRegistrationForDetails(id);
		Race race = registration.getRaceCategory().getRace();
		List<User> organizers = UserDAO.getInstance().findOrganizers(race.getEvent());
		User member = loadMember(memberId);
		User loggedInUser = User.getLoggedIn();

		if (!loggedInUser.getAdmin() && !organizers.contains(loggedInUser)) {
			throw new ForbiddenException();
		}

		UserRegistration teamFormation = UserRegistrationDAO.getInstance().load(registration, member);
		if (teamFormation == null) {
			throw new UnprocessableEntityException().addViolation("member", member.getProfile().getName()
					+ " não faz parte da equipe " + registration.getTeamName());
		}

		if (registration.getPayment().getTransaction() != null) {
			throw new UnprocessableEntityException().addViolation("transaction", "O pagamento já está em andamento");
		}

		if (price.doubleValue() < 0) {
			throw new UnprocessableEntityException().addViolation("price", "Valor inválido");
		}

		teamFormation.setRacePrice(price);
		UserRegistrationDAO.getInstance().update(teamFormation);

		RegistrationDAO registrationDAO = RegistrationDAO.getInstance();
		Registration persisted = registrationDAO.load(id);

		if (persisted.getPayment() != null) {
			persisted.getPayment().setCode(null);
			registrationDAO.update(persisted);
		}
	}

	@PUT
	@LoggedIn
	@Transactional
	@Path("{id}/team/name")
	@Produces("application/json")
	public void updateTeamName(@PathParam("id") Long id, String teamName) throws Exception {
		Registration registration = loadRegistrationForDetails(id);
		Race race = registration.getRaceCategory().getRace();
		List<User> organizers = UserDAO.getInstance().findOrganizers(race.getEvent());
		List<User> team = UserDAO.getInstance().findUserRegistrations(registration);
		User loggedInUser = User.getLoggedIn();

		if (!loggedInUser.getAdmin() && !organizers.contains(loggedInUser) && !team.contains(loggedInUser)) {
			throw new ForbiddenException();
		}

		if (race.getStatus().getId() != OPEN_ID) {
			throw new UnprocessableEntityException().addViolation("period", "fora do período permitido");
		}

		if (Strings.isEmpty(teamName)) {
			throw new UnprocessableEntityException().addViolation("name", "campo obrigatório");
		}

		RegistrationDAO registrationDAO = RegistrationDAO.getInstance();
		Registration persisted = registrationDAO.load(id);
		persisted.setTeamName(teamName);
		registrationDAO.update(persisted);
	}

	private String createCode(Registration registration, URI baseUri) throws Exception {
		List<BasicNameValuePair> payload = new ArrayList<BasicNameValuePair>();
		payload.add(new BasicNameValuePair("email", registration.getRaceCategory().getRace().getEvent().getPayment()
				.getAccount()));
		payload.add(new BasicNameValuePair("token", registration.getRaceCategory().getRace().getEvent().getPayment()
				.getToken()));
		payload.add(new BasicNameValuePair("currency", "BRL"));
		payload.add(new BasicNameValuePair("reference", registration.getFormattedId()));
		payload.add(new BasicNameValuePair("redirectURL", baseUri.toString() + "registration/"
				+ registration.getFormattedId()));

		NumberFormat numberFormat = NumberFormat.getNumberInstance(US);
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);

		int i = 0;
		List<UserRegistration> teamFormations = UserRegistrationDAO.getInstance().find(registration);
		for (UserRegistration teamFormation : teamFormations) {
			if (teamFormation.getRacePrice().doubleValue() > 0) {
				i++;
				payload.add(new BasicNameValuePair("itemId" + i, String.valueOf(i)));
				payload.add(new BasicNameValuePair("itemDescription" + i, "Inscrição de "
						+ teamFormation.getUser().getName()));
				payload.add(new BasicNameValuePair("itemAmount" + i, numberFormat.format(teamFormation.getRacePrice())));
				payload.add(new BasicNameValuePair("itemQuantity" + i, "1"));
			}
		}

		User user = User.getLoggedIn();
		payload.add(new BasicNameValuePair("senderName", user.getProfile().getName()));
		payload.add(new BasicNameValuePair("senderEmail", user.getEmail()));

		HttpPost request = new HttpPost("https://ws.pagseguro.uol.com.br/v2/checkout/");
		String entity = URLEncodedUtils.format(payload, "UTF-8");
		request.setEntity(new StringEntity(entity));
		request.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8;");

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		String body = Strings.parse(response.getEntity().getContent());
		String code = null;

		if (response.getStatusLine().getStatusCode() == 200 && body != null) {
			Pattern pattern = Pattern.compile("<checkout>.*<code>(\\w+)</code>.*</checkout>");
			Matcher matcher = pattern.matcher(body);

			if (matcher.find()) {
				code = matcher.group(1);
			}
		}

		if (code == null) {
			String message = "Falha na comunicação com o PagSeguro.";
			Logger logger = Beans.getReference(Logger.class, new NameQualifier(RegistrationREST.class.getName()));
			logger.severe(message + ":" + response.toString());
			System.out.println(response.toString());
			throw new HttpViolationException(502).addViolation(message);
		}

		return code;
	}

	private Registration loadRegistrationForDetails(Long id) throws Exception {
		Registration result = RegistrationDAO.getInstance().loadForDetails(id);

		if (result == null) {
			throw new NotFoundException();
		}

		return result;
	}

	private User loadMember(Integer id) throws Exception {
		User result = UserDAO.getInstance().loadBasics(id);

		if (result == null) {
			throw new UnprocessableEntityException().addViolation("member", "Usuário inválido");
		}

		return result;
	}
}
