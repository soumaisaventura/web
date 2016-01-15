package adventure.business;

import static adventure.util.Constants.EVENT_BANNER_HEIGHT;
import static adventure.util.Constants.EVENT_BANNER_WIDTH;
import static net.coobird.thumbnailator.geometry.Positions.CENTER;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.coobird.thumbnailator.Thumbnails;
import adventure.entity.Event;
import adventure.persistence.EventDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;

public class EventBusiness {

	public static EventBusiness getInstance() {
		return Beans.getReference(EventBusiness.class);
	}

	@Transactional
	public void updateBanner(Event event, InputStream inputStream) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Thumbnails.of(inputStream).crop(CENTER).size(EVENT_BANNER_WIDTH, EVENT_BANNER_HEIGHT).keepAspectRatio(true)
				.toOutputStream(outputStream);
		byte image[] = outputStream.toByteArray();

		event.setBanner(image);
		EventDAO.getInstance().update(event);
	}
}
