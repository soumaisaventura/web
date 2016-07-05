package adventure.entity;

import java.io.InputStream;

public class Picture {

    private InputStream inputStream;

    private String contentType;

    public Picture(InputStream inputStream, String contentType) {
        this.inputStream = inputStream;
        this.contentType = contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getContentType() {
        return contentType;
    }
}