package rest.v1.service;

import br.gov.frameworkdemoiselle.transaction.Transactional;
import core.entity.Event;
import core.entity.Profile;
import core.persistence.EventDAO;
import core.persistence.ProfileDAO;
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
