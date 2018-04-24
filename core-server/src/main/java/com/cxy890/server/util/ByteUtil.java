package com.cxy890.server.util;

import lombok.Cleanup;
import sun.misc.BASE64Decoder;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;

/**
 * @author BD-PC27
 */
public class ByteUtil {

    public static byte[] getIcon() throws IOException {
        try {
            return image2byte("favicon.ico");
        } catch (Exception e) {
            BASE64Decoder decoder = new BASE64Decoder();
            return decoder.decodeBuffer(icon);
        }
    }

    private static final String icon = "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";

    /**
     * 图片到byte数组
     *
     * @return byte[]
     * @throws IOException IOException
     */
    public static byte[] image2byte(String file) throws IOException {
        @Cleanup FileImageInputStream input = new FileImageInputStream(new File(file));
        @Cleanup ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int numBytesRead;
        while ((numBytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, numBytesRead);
        }
        return output.toByteArray();
    }

}
