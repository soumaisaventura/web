package adventure.rest;

import static adventure.entity.StatusType.CANCELLED;
import static adventure.entity.StatusType.CONFIRMED;
import static adventure.entity.StatusType.PENDENT;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import adventure.entity.Race;
import adventure.entity.Registration;
import adventure.entity.StatusType;
import adventure.persistence.RaceDAO;
import adventure.persistence.RegistrationDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.NameQualifier;
import br.gov.frameworkdemoiselle.util.Strings;

@Path("registration/notification")
public class RegistrationNotificationREST {

	private transient Logger logger;

	@POST
	@Transactional
	@Consumes("application/x-www-form-urlencoded")
	public void confirm(@FormParam("notificationCode") String code, @FormParam("notificationType") String type)
			throws Exception {

		getLogger().info("Recebendo [notificationCode=" + code + "] e [notificationType=" + type + "].");

		if ("transaction".equalsIgnoreCase(type)) {
			for (Race race : RaceDAO.getInstance().findOpen()) {
				String body = getBody(code, race);
				Long registerId = parseRegiserId(body);
				StatusType statusType = parseStatus(body);

				if (registerId != null && statusType != null) {
					update(registerId, statusType, race);
				}
			}
		}
	}

	private void update(Long registrationId, StatusType status, Race race) {
		RegistrationDAO dao = RegistrationDAO.getInstance();
		Registration registration = dao.loadForDetails(registrationId);

		if (registration != null && registration.getStatus() == PENDENT
				&& race.equals(registration.getRaceCategory().getRace())) {
			Registration persistedRegistration = dao.load(registrationId);
			persistedRegistration.setStatus(status);
			persistedRegistration.setDate(new Date());

			dao.update(persistedRegistration);
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

	private StatusType parseStatus(String body) {
		StatusType statusType = null;

		if (body != null) {
			Pattern pattern = Pattern.compile("<transaction>.*<status>(\\d)</status>.*</transaction>");
			Matcher matcher = pattern.matcher(body);

			int status = 0;
			if (matcher.find()) {
				status = Integer.parseInt(matcher.group(1));
			}

			switch (status) {
				case 3:
					statusType = CONFIRMED;
					break;

				case 6:
				case 7:
					statusType = CANCELLED;
					break;
			}
		}

		return statusType;
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
