package adventure.business;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import adventure.entity.Profile;
import adventure.persistence.ProfileDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

@Stateless
public class ProfileBusiness {

	public static ProfileBusiness getInstance() {
		return Beans.getReference(ProfileBusiness.class);
	}

	@Asynchronous
	@Transactional
	public void updatePicture(Integer profileId, String pictureUrl) throws Exception {
		byte[] picture = getPicture(new URL(pictureUrl));

		if (picture != null) {
			ProfileDAO profileDAO = ProfileDAO.getInstance();
			Profile profile = profileDAO.load(profileId);
			profile.setPicture(picture);
			profileDAO.update(profile);
		}
	}

	private byte[] getPicture(URL url) throws Exception {
		byte[] result = null;

		if (url != null) {
			// try {
			// URL _url = new URL(url);

			HttpGet request = new HttpGet(url.toString());
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);

			int status = response.getStatusLine().getStatusCode();

			// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// conn.setInstanceFollowRedirects(false);
			// conn.connect();
			// int status = conn.getResponseCode();

			if (status == 301 || status == 302) {
				// String location = conn.getHeaderField("Location");
				Header[] headers = response.getHeaders("Location");
				String location = headers.length > 0 ? headers[0].getValue() : null;
				// String location = response.getHeaders("Location")[0];
				result = getPicture(new URL(location));

			} else if (status == 200) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				result = out.toByteArray();
				// result = Strings.parse(conn.getInputStream()).getBytes();
			} else {
				result = null;
			}

			// if (!conn.getURL().equals(url)) {
			// } else {
			// result = content.getBytes();
			// }

			// } catch (Exception cause) {
			// // TODO Fazer log da exceção
			// cause.printStackTrace();
			// }
		}

		return result;
	}

}
