package adventure.business;

import adventure.entity.Picture;
import adventure.entity.Profile;
import adventure.persistence.ProfileDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import static adventure.util.Constants.USER_PHOTO_HEIGHT;
import static adventure.util.Constants.USER_PHOTO_WIDTH;
import static net.coobird.thumbnailator.geometry.Positions.CENTER;

@Stateless
public class ProfileBusiness {

    public static ProfileBusiness getInstance() {
        return Beans.getReference(ProfileBusiness.class);
    }

    @Asynchronous
    @Transactional
    public void updatePicture(Integer profileId, String pictureUrl) throws Exception {
        Picture picture = getPicture(new URL(pictureUrl));
        updatePicture(profileId, picture);
    }

    @Transactional
    public void updatePicture(Integer profileId, Picture picture) throws Exception {
        if (picture != null && picture.getInputStream() != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(picture.getInputStream()).crop(CENTER).size(USER_PHOTO_WIDTH, USER_PHOTO_HEIGHT)
                    .keepAspectRatio(true).outputFormat("jpg").toOutputStream(outputStream);
            byte image[] = outputStream.toByteArray();
            String hash = DigestUtils.md5Hex(image);

            ProfileDAO profileDAO = ProfileDAO.getInstance();
            Profile profile = profileDAO.load(profileId);
            profile.setPicture(image);
            profile.setPictureHash(hash);
            profileDAO.update(profile);
        }
    }

    private Picture getPicture(URL url) throws Exception {
        Picture picture = null;

        if (url != null) {
            HttpGet request = new HttpGet(url.toString());
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            int status = response.getStatusLine().getStatusCode();

            if (status == 301 || status == 302) {
                String location = getHeader("Location", response);
                picture = getPicture(new URL(location));
            } else if (status == 200) {
                picture = new Picture(response.getEntity().getContent(), getHeader("Content-Type", response));
            }
        }

        return picture;
    }

    private String getHeader(String key, HttpResponse response) {
        Header[] headers = response.getHeaders(key);
        return headers.length > 0 ? headers[0].getValue() : null;
    }
}
