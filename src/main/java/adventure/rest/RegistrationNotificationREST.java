package adventure.rest;

import static adventure.entity.StatusType.CONFIRMED;
import static adventure.entity.StatusType.PENDENT;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import adventure.entity.Race;
import adventure.entity.Registration;
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

	@POST
	@Transactional
	@Path("notification")
	@Consumes("application/x-www-form-urlencoded")
	public void confirm(@FormParam("notificationCode") String code, @FormParam("notificationType") String type,
			@Context UriInfo uriInfo) throws Exception {

		getLogger().info("Recebendo [notificationCode=" + code + "] e [notificationType=" + type + "].");

		if ("transaction".equalsIgnoreCase(type)) {
			for (Race race : RaceDAO.getInstance().findOpenAutoPayment()) {
				String body = getBody(code, race);
				Transaction transaction = parse(body);

				update(race, transaction, uriInfo);
			}
		}
	}

	private void update(Race race, Transaction transaction, UriInfo uriInfo) throws Exception {
		RegistrationDAO dao = RegistrationDAO.getInstance();
		Registration registration = dao.loadForDetails(transaction.reference);

		if (registration != null && registration.getStatus() == PENDENT
				&& race.equals(registration.getRaceCategory().getRace())) {
			URI baseUri = uriInfo.getBaseUri().resolve("..");
			Registration persistedRegistration = dao.load(transaction.reference);

			switch (transaction.status) {
				case 1: // iniciada
					persistedRegistration.setPaymentTransaction(transaction.code);
					dao.update(persistedRegistration);
					break;

				case 3: // paga
					persistedRegistration.setPaymentTransaction(transaction.code);
					persistedRegistration.setStatus(CONFIRMED);
					persistedRegistration.setDate(new Date());
					dao.update(persistedRegistration);

					MailDAO.getInstance().sendRegistrationConfirmation(registration, baseUri);
					break;

				case 7: // cancelada
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

	private Transaction parse(String body) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Transaction.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(body);
		Transaction transaction = (Transaction) unmarshaller.unmarshal(reader);

		return transaction;
	}

	private Logger getLogger() {
		if (this.logger == null) {
			this.logger = Beans.getReference(Logger.class,
					new NameQualifier(RegistrationNotificationREST.class.getName()));
		}

		return this.logger;
	}

	// public static void main(String[] args) throws Exception {
	// File file = new File("/Users/zyc/Workspace/zyc/adventure/target/x.xml");
	//
	// RegistrationNotificationREST r = new RegistrationNotificationREST();
	// Transaction transaction = r.parse(FileUtils.openInputStream(file));
	//
	// System.out.println(transaction);
	// }

	@XmlAccessorType(FIELD)
	@XmlRootElement(name = "transaction")
	static class Transaction {

		@XmlElement(name = "code")
		String code;

		@XmlElement(name = "status")
		int status;

		@XmlElement(name = "reference")
		Long reference;
	}
}
