package com.cxy890.config.loader.environment;

import com.cxy890.config.ConfigKeys;
import lombok.Cleanup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.cxy890.config.loader.environment.EnvironmentLoader.APP;

/**
 * @author BD-PC27
 */
public final class PropHelper {


    private static Properties PROP = new Properties();

    static Object getValue(String key) {
        return PROP.getProperty(key);
    }

    static boolean load() throws IOException {
        if(loadByName(APP)) {
            Object active = getValue(ConfigKeys.APPLICATION_ACTIVE);
            if (active != null)
                loadByName(APP + "-" + active);
            return true;
        }
        return false;
    }

    private static boolean loadByName(String application) throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        @Cleanup InputStream resourceAsStream = contextClassLoader.getResourceAsStream(application + ".properties");
        if (resourceAsStream != null)
            PROP.load(resourceAsStream);

        return !PROP.isEmpty();
    }

}
