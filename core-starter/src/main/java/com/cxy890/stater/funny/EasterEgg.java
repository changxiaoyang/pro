package com.cxy890.stater.funny;

import com.cxy890.config.util.Strings;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author ChangXiaoyang
 */
@Slf4j
public class EasterEgg {

    public static void printBanner() {
        log.info(EasterEgg.getBanner());
    }

    private static String getBanner() {
        String bnr;
        return ((bnr = getFromFile()) != null) ? "\n\r" + bnr : banner;
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

    /**
     * keep mysterious
     */
    private final static String banner = " \n\r ============= Welcome, my little darling. ============= \n\r"
            + Strings.decode("ddRddRddyddxddxddxddxddxddxddyddyddxddxddxddxddxddxddyddyddxddxddxddxddxddxddy" +
            "ddZddRddRddRdgdd4NddyddxddxddyddRdgddgdd4PddyddxddxddyddRdgddgdd4UddyddxddxddyddRdgdddZddRddRddRdgdddRddzdd" +
            "pd4MddzddRdgddgdddRddzddpd4MddzddRdgddgdddRddOd4MddpddoddRdgdddZddRddRddRdgdddRddzd4MddpddzddRdgddgdddRddOd" +
            "4Sd4SddoddRdgddgdddRddzd4MddpddzddRdgdddZddRddRddRdgdddRddLddxddxddLd4NdgddgdddRddLddxddxddLd4PdgddgdddRddL" +
            "ddxddxddLd4UdgdddZddRddRddRd4TddxddxddxddxddxddxddLd4TddxddxddxddxddxddxddLd4TddxddxddxddxddxddxddL");

}
