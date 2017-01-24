package core.business;

import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import core.entity.Event;
import core.entity.Picture;
import core.persistence.EventDAO;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;

import static core.util.Constants.EVENT_BANNER_HEIGHT;
import static core.util.Constants.EVENT_BANNER_WIDTH;
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
            event.setBanner(outputStream.toByteArray());
            EventDAO.getInstance().update(event);
        }
    }
}
