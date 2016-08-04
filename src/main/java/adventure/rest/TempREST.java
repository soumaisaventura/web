package adventure.rest;

import adventure.entity.Event;
import adventure.entity.Profile;
import adventure.persistence.EventDAO;
import adventure.persistence.ProfileDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import org.apache.commons.codec.digest.DigestUtils;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("temp")
public class TempREST {

    @POST
    @Transactional
    public void temp() throws Exception {
        EventDAO eventDAO = EventDAO.getInstance();
        for (Event event : eventDAO.findAll()) {
            if (event.getBanner() != null && event.getBannerHash() == null) {
                event.setBannerHash(DigestUtils.md5Hex(event.getBanner()));
                eventDAO.update(event);
            }
        }

        ProfileDAO profileDAO = ProfileDAO.getInstance();
        for (Profile profile : profileDAO.findAll()) {
            if (profile.getPicture() != null && profile.getPictureHash() == null) {
                profile.setPictureHash(DigestUtils.md5Hex(profile.getPicture()));
                profileDAO.update(profile);
            }
        }
    }
}
