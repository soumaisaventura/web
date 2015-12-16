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
import adventure.entity.PaymentType;
import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.RegistrationStatusType;
import adventure.entity.TeamFormation;
import adventure.entity.User;
import adventure.persistence.RegistrationDAO;
import adventure.persistence.TeamFormationDAO;
import adventure.persistence.UserDAO;
import adventure.rest.LocationREST.CityData;
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
			data.teamName = registration.getTeamName();
			data.status = registration.getStatus();
			data.race = new RaceData();
			data.race.id = registration.getRaceCategory().getRace().getId();
			data.race.name = registration.getRaceCategory().getRace().getName();
			data.race.date = registration.getRaceCategory().getRace().getDate();
			data.race.city = new CityData();
			data.race.city.id = registration.getRaceCategory().getRace().getCity().getId();
			data.race.city.name = registration.getRaceCategory().getRace().getCity().getName();
			data.race.city.state = registration.getRaceCategory().getRace().getCity().getState().getAbbreviation();

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

		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(registration.getRaceCategory().getRace());
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

		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(registration.getRaceCategory().getRace());
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
		data.submitter = new UserData();
		data.submitter.id = registration.getSubmitter().getId();
		data.submitter.email = registration.getSubmitter().getEmail();
		data.submitter.name = registration.getSubmitter().getProfile().getName();
		data.teamName = registration.getTeamName();
		data.payment = new PaymentData();
		data.payment.type = registration.getRaceCategory().getRace().getPaymentType();
		data.payment.info = registration.getRaceCategory().getRace().getPaymentInfo();
		data.payment.code = registration.getPaymentCode();
		data.payment.transaction = registration.getPaymentTransaction();

		List<TeamFormation> teamFormations = TeamFormationDAO.getInstance().find(registration);
		Race race = registration.getRaceCategory().getRace();
		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		User loggedInUser = User.getLoggedIn();

		if (!loggedInUser.getAdmin() && !registration.getSubmitter().equals(loggedInUser)
				&& !teamFormations.contains(new TeamFormation(registration, loggedInUser))
				&& !organizers.contains(loggedInUser)) {
			throw new ForbiddenException();
		}

		for (TeamFormation teamFormation : teamFormations) {
			UserData member = new UserData();
			member.id = teamFormation.getUser().getId();
			member.email = teamFormation.getUser().getEmail();
			member.name = teamFormation.getUser().getProfile().getName();
			member.phone = teamFormation.getUser().getProfile().getMobile();

			member.bill = new BillData();
			member.bill.racePrice = teamFormation.getRacePrice().floatValue();
			data.teamFormation.add(member);
		}

		data.race = new RaceData();
		data.race.id = race.getId();
		data.race.name = race.getName();
		data.race.date = race.getDate();
		data.race.city = new CityData();
		data.race.city.id = race.getCity().getId();
		data.race.city.name = race.getCity().getName();
		data.race.city.state = race.getCity().getState().getAbbreviation();
		data.race.status = race.getStatus().getName();

		for (User user : UserDAO.getInstance().findRaceOrganizers(race)) {
			UserData organizer = new UserData();
			organizer.id = user.getId();
			organizer.email = user.getEmail();
			organizer.name = user.getProfile().getName();
			organizer.phone = user.getProfile().getMobile();
			data.race.organizers.add(organizer);
		}

		data.course = new CourseData();
		data.course.id = registration.getRaceCategory().getCourse().getId();
		data.course.name = registration.getRaceCategory().getCourse().getName();
		data.category = new CategoryData();
		data.category.id = registration.getRaceCategory().getCategory().getId();
		data.category.name = registration.getRaceCategory().getCategory().getName();

		return data;
	}

	@POST
	@LoggedIn
	@Transactional
	@Path("{id}/payment")
	@Produces("text/plain")
	public Response sendPayment(@PathParam("id") Long id, @Context UriInfo uriInfo) throws Exception {
		Registration registration = loadRegistrationForDetails(id);
		String code = registration.getPaymentCode();
		int status;

		if (code == null) {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			code = createCode(registration, baseUri);

			Registration persistedRegistration = RegistrationDAO.getInstance().load(registration.getId());
			persistedRegistration.setPaymentCode(code);
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
		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		User member = loadMember(memberId);
		User loggedInUser = User.getLoggedIn();

		if (!loggedInUser.getAdmin() && !organizers.contains(loggedInUser)) {
			throw new ForbiddenException();
		}

		TeamFormation teamFormation = TeamFormationDAO.getInstance().load(registration, member);
		if (teamFormation == null) {
			throw new UnprocessableEntityException().addViolation("member", member.getProfile().getName()
					+ " não faz parte da equipe " + registration.getTeamName());
		}

		if (registration.getPaymentTransaction() != null) {
			throw new UnprocessableEntityException().addViolation("transaction", "O pagamento já está em andamento");
		}

		if (price.doubleValue() < 0) {
			throw new UnprocessableEntityException().addViolation("price", "Valor inválido");
		}

		teamFormation.setRacePrice(price);
		TeamFormationDAO.getInstance().update(teamFormation);

		RegistrationDAO registrationDAO = RegistrationDAO.getInstance();
		Registration persisted = registrationDAO.load(id);
		persisted.setPaymentCode(null);
		registrationDAO.update(persisted);
	}

	@PUT
	@LoggedIn
	@Transactional
	@Path("{id}/team/name")
	@Produces("application/json")
	public void updateTeamName(@PathParam("id") Long id, String teamName) throws Exception {
		Registration registration = loadRegistrationForDetails(id);
		Race race = registration.getRaceCategory().getRace();
		List<User> organizers = UserDAO.getInstance().findRaceOrganizers(race);
		List<User> team = UserDAO.getInstance().findTeamFormation(registration);
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
		payload.add(new BasicNameValuePair("email", registration.getRaceCategory().getRace().getPaymentAccount()));
		payload.add(new BasicNameValuePair("token", registration.getRaceCategory().getRace().getPaymentToken()));
		payload.add(new BasicNameValuePair("currency", "BRL"));
		payload.add(new BasicNameValuePair("reference", registration.getFormattedId()));
		payload.add(new BasicNameValuePair("redirectURL", baseUri.toString() + "registration/"
				+ registration.getFormattedId()));

		NumberFormat numberFormat = NumberFormat.getNumberInstance(US);
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);

		int i = 0;
		List<TeamFormation> teamFormations = TeamFormationDAO.getInstance().find(registration);
		for (TeamFormation teamFormation : teamFormations) {
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

	public static class RegistrationData {

		public Long id;

		public String number;

		public Date date;

		public RegistrationStatusType status;

		public PaymentData payment;

		public UserData submitter;

		public RaceData race;

		public CourseData course;

		public CategoryData category;

		public String teamName;

		public List<UserData> teamFormation = new ArrayList<UserData>();

	}

	public static class RaceData {

		public Integer id;

		public String name;

		public Date date;

		public CityData city;

		public List<UserData> organizers = new ArrayList<UserData>();

		public String status;
	}

	public static class CourseData {

		public Integer id;

		public String name;
	}

	public static class CategoryData {

		public Integer id;

		public String name;
	}

	public static class UserData {

		public Integer id;

		public String name;

		public String email;

		public String phone;

		public String city;

		public String state;

		public BillData bill;
	}

	public static class BillData {

		public float racePrice;
	}

	public static class PaymentData {

		public PaymentType type;

		public String info;

		public String code;

		public String transaction;
	}
}
