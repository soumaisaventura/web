package adventure.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.StatusType;
import adventure.persistence.MailDAO;
import adventure.persistence.RaceDAO;
import adventure.persistence.RegistrationDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.NameQualifier;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("registration")
public class RegistrationNotificationREST {

	private transient Logger logger;

	enum PaymentStatus {
		CONFIRMED, CANCELLED
	}

	@POST
	@Transactional
	@Path("notification")
	@Consumes("application/x-www-form-urlencoded")
	public void confirm(@FormParam("notificationCode") String code, @FormParam("notificationType") String type,
			@Context UriInfo uriInfo) throws Exception {

		getLogger().info("Recebendo [notificationCode=" + code + "] e [notificationType=" + type + "].");

		if ("transaction".equalsIgnoreCase(type)) {
			for (Race race : RaceDAO.getInstance().findOpen()) {
				String body = getBody(code, race);
				Long registerId = parseRegiserId(body);
				PaymentStatus status = parseStatus(body);

				if (registerId != null && status != null) {
					update(registerId, status, race, uriInfo);
				}
			}
		}
	}

	private void update(Long registrationId, PaymentStatus status, Race race, UriInfo uriInfo) throws Exception {
		RegistrationDAO dao = RegistrationDAO.getInstance();
		Registration registration = dao.loadForDetails(registrationId);

		if (registration != null && registration.getStatus() == StatusType.PENDENT
				&& race.equals(registration.getRaceCategory().getRace())) {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			Registration persistedRegistration = dao.load(registrationId);

			if (status == PaymentStatus.CONFIRMED) {
				persistedRegistration.setStatus(StatusType.CONFIRMED);
				persistedRegistration.setDate(new Date());
				dao.update(persistedRegistration);

				MailDAO.getInstance().sendRegistrationConfirmation(registration, baseUri);

			} else if (status == PaymentStatus.CANCELLED) {
				persistedRegistration.setPaymentTransaction(null);
				dao.update(persistedRegistration);
			}
		}
	}

	private String getBody(String code, Race race) throws Exception {
		String result = null;

		if (code != null) {
			List<BasicNameValuePair> payload = new ArrayList<BasicNameValuePair>();
			payload.add(new BasicNameValuePair("email", race.getPaymentAccount()));
			payload.add(new BasicNameValuePair("token", race.getPaymentToken()));

			String url = "";
			url += "https://ws.pagseguro.uol.com.br/v2/transactions/notifications/" + code;
			url += "?" + URLEncodedUtils.format(payload, "UTF-8");

			getLogger().info("Invocando [" + url + "]");

			HttpGet request = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);

			getLogger().info("Status recebido [" + response.getStatusLine().getStatusCode() + "]");

			if (response.getStatusLine().getStatusCode() == 200) {
				result = Strings.parse(response.getEntity().getContent());
				getLogger().fine("Body recebido [" + result + "]");
			}
		}

		return result;
	}

	private PaymentStatus parseStatus(String body) {
		PaymentStatus paymentStatus = null;

		if (body != null) {
			Pattern pattern = Pattern.compile("<transaction>.*<status>(\\d)</status>.*</transaction>");
			Matcher matcher = pattern.matcher(body);

			int status = 0;
			if (matcher.find()) {
				status = Integer.parseInt(matcher.group(1));
			}

			switch (status) {
				case 3:
					paymentStatus = PaymentStatus.CONFIRMED;
					break;

				case 7:
					paymentStatus = PaymentStatus.CANCELLED;
					break;
			}
		}

		return paymentStatus;
	}

	private Long parseRegiserId(String body) {
		Long result = null;

		if (body != null) {
			Pattern pattern = Pattern.compile("<transaction>.*<reference>(\\d+)</reference>.*</transaction>");
			Matcher matcher = pattern.matcher(body);

			if (matcher.find()) {
				result = Long.parseLong(matcher.group(1));
			}
		}

		return result;
	}

	private Logger getLogger() {
		if (this.logger == null) {
			this.logger = Beans.getReference(Logger.class,
					new NameQualifier(RegistrationNotificationREST.class.getName()));
		}

		return this.logger;
	}
}
