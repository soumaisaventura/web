package adventure.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import net.coobird.thumbnailator.Thumbnails;
import br.gov.frameworkdemoiselle.util.Beans;

public class ImageBusiness {

	public static ImageBusiness getInstance() {
		return Beans.getReference(ImageBusiness.class);
	}

	public byte[] resize(byte[] image, Integer defaultWidth, Integer width) throws Exception {
		byte[] result = image;

		if (image != null && width != null && width < defaultWidth) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Thumbnails.of(new ByteArrayInputStream(result)).scale((double) width / defaultWidth).toOutputStream(out);
			result = out.toByteArray();
		}

		return result;
	}
}
