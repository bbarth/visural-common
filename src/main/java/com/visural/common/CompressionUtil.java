package com.visural.common;

import com.google.common.io.ByteStreams;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * @author Richard Nichols
 */
public class CompressionUtil {

    public static byte[] gzip(byte[] data) throws IOException {
        GZIPOutputStream gzip = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            gzip = new GZIPOutputStream(out);
            ByteStreams.copy(in, gzip);
            gzip.close();
            return out.toByteArray();
        } finally {
            IOUtil.silentClose(CompressionUtil.class, gzip);
        }
    }

    public static byte[] gunzip(byte[] data) throws IOException {
        InputStream in = new GZIPInputStream(new ByteArrayInputStream(data));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteStreams.copy(in, out);
        return out.toByteArray();
    }
}
