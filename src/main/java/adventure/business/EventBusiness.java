package adventure.business;

import adventure.entity.Event;
import adventure.entity.Picture;
import adventure.persistence.EventDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayOutputStream;

import static adventure.util.Constants.EVENT_BANNER_HEIGHT;
import static adventure.util.Constants.EVENT_BANNER_WIDTH;
import static net.coobird.thumbnailator.geometry.Positions.CENTER;

public class EventBusiness {

    public static EventBusiness getInstance() {
        return Beans.getReference(EventBusiness.class);
    }

    @Transactional
    public void updateBanner(Event event, Picture picture) throws Exception {
        if (picture != null && picture.getInputStream() != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(picture.getInputStream()).crop(CENTER).size(EVENT_BANNER_WIDTH, EVENT_BANNER_HEIGHT)
                    .keepAspectRatio(true).outputFormat("png").toOutputStream(outputStream);
            byte image[] = outputStream.toByteArray();
            String tag = DigestUtils.md5Hex(image);

            event.setBanner(image);
            event.setBannerTag(tag);
            EventDAO.getInstance().update(event);
        }
    }
}
