package com.nhiennhatt.bookstoreapi.utils;

import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.IOException;
import java.io.InputStream;

public class MimeTypeUtil {
    private static final Tika tika = new Tika();

    public static String getExtension(InputStream file) throws IOException, MimeTypeException {
        String mimeTypeStr = tika.detect(file);
        MimeType mimeType = MimeTypes.getDefaultMimeTypes().forName(mimeTypeStr);
        return mimeType.getExtension();
    }
}
