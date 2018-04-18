package com.cxy890.stater.funny;

import com.cxy890.config.util.StringUtil;
import lombok.Cleanup;

import java.io.*;

/**
 * @author BD-PC27
 */
public class EasterEgg {

    /**
     * keep mysterious
     */
    private final static String banner = "ddRddRddyddxddxddxddxddxddxddyddyddxddxddxddxddxddxddyddyddxddxddxddxddxddxddy" +
            "ddZddRddRddRdgdd4NddyddxddxddyddRdgddgdd4PddyddxddxddyddRdgddgdd4UddyddxddxddyddRdgdddZddRddRddRdgdddRddzdd" +
            "pd4MddzddRdgddgdddRddzddpd4MddzddRdgddgdddRddOd4MddpddoddRdgdddZddRddRddRdgdddRddzd4MddpddzddRdgddgdddRddOd" +
            "4Sd4SddoddRdgddgdddRddzd4MddpddzddRdgdddZddRddRddRdgdddRddLddxddxddLd4NdgddgdddRddLddxddxddLd4PdgddgdddRddL" +
            "ddxddxddLd4UdgdddZddRddRddRd4TddxddxddxddxddxddxddLd4TddxddxddxddxddxddxddLd4TddxddxddxddxddxddxddL";

    public static String getBanner() {
        String bnr;
        if ((bnr = getFromFile()) != null) return bnr;
        return StringUtil.decode(banner);
    }

    private static String getFromFile() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            @Cleanup InputStream resource = classLoader.getResourceAsStream("banner.bnr");
            if (resource != null) {
                StringBuilder builder = new StringBuilder();
                @Cleanup InputStreamReader streamReader = new InputStreamReader(resource);
                @Cleanup BufferedReader bufferedReader = new BufferedReader(streamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    builder.append(line).append(System.lineSeparator());
                return builder.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}
