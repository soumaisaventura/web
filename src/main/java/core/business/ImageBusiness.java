package core.business;

import br.gov.frameworkdemoiselle.util.Beans;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageBusiness {

    public static ImageBusiness getInstance() {
        return Beans.getReference(ImageBusiness.class);
    }

    public byte[] resize(byte[] image, Integer defaultWidth, Integer width) throws Exception {
        byte[] result = image;

        if (image != null && width != null && width < defaultWidth) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Thumbnails.of(new ByteArrayInputStream(result)).scale((double) width / defaultWidth).toOutputStream(out);
                result = out.toByteArray();

            } catch (UnsupportedFormatException cause) {
                result = image;
            }
        }

        return result;
    }
}
