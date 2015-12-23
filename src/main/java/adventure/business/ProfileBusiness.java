package adventure.business;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

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
		InputStream inputStream = getPicture(new URL(pictureUrl));

		if (inputStream != null) {
			updatePicture(profileId, inputStream);
		}
	}

	@Transactional
	public void updatePicture(Integer profileId, InputStream inputStream) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Thumbnails.of(inputStream).crop(Positions.CENTER).size(250, 250).keepAspectRatio(true)
				.toOutputStream(outputStream);
		byte picture[] = outputStream.toByteArray();

		ProfileDAO profileDAO = ProfileDAO.getInstance();
		Profile profile = profileDAO.load(profileId);
		profile.setPicture(picture);
		profileDAO.update(profile);
	}

	private InputStream getPicture(URL url) throws Exception {
		InputStream result = null;

		if (url != null) {
			HttpGet request = new HttpGet(url.toString());
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);
			int status = response.getStatusLine().getStatusCode();

			if (status == 301 || status == 302) {
				Header[] headers = response.getHeaders("Location");
				String location = headers.length > 0 ? headers[0].getValue() : null;
				result = getPicture(new URL(location));
			} else if (status == 200) {
				result = response.getEntity().getContent();
			} else {
				result = null;
			}
		}

		return result;
	}

}
