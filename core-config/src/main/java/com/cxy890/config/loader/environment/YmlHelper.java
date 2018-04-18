package com.cxy890.config.loader.environment;

import lombok.Cleanup;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.cxy890.config.ConfigKeys.APPLICATION_ACTIVE;
import static com.cxy890.config.loader.environment.EnvironmentLoader.APP;

/**
 * @author BD-PC27
 */
public final class YmlHelper {

    private static Map<String, Object> Y_MAP = new HashMap<>();

    static boolean load() throws IOException {
        loadYml(APP);
        Object activePro = getValue(APPLICATION_ACTIVE);
        if (activePro != null) {
            loadYml(APP + "-" + activePro);
        }

        return !Y_MAP.isEmpty();
    }

    private static void loadYml(String resourceName) throws IOException {
        Yaml yaml = new Yaml();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        URL resource = contextClassLoader.getResource(resourceName + ".yaml");
        if (resource == null) {
            resource = contextClassLoader.getResource(resourceName + ".yml");
        }
        assert resource != null;
        @Cleanup FileInputStream inputStream = new FileInputStream(resource.getFile());
        Y_MAP.putAll(yaml.load(inputStream));
    }

    static Object getValue(String keyStr) {
        String[] keys = keyStr.split("\\.");
        Object value = null;
        for (String key : keys) {
            if (value instanceof Map)
                value = ((Map)value).get(key);
            else
                value = Y_MAP.get(key);
        }
        return value;
    }

}
