package com.cxy890.config.loader.environment;

import com.cxy890.config.util.FileUtil;
import lombok.Cleanup;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.cxy890.config.ConfigKeys.APPLICATION_ACTIVE;
import static com.cxy890.config.loader.environment.EnvironmentLoader.APP;

/**
 * @author ChangXiaoyang
 */
final class YmlHelper {

    private static Map<String, Object> Y_MAP = new HashMap<>();

    private static Properties PROP = new Properties();

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
        String filePath = FileUtil.getProjectPath() + resourceName;
        if (FileUtil.exists(filePath + ".yml")) {
            @Cleanup FileInputStream inputStream = new FileInputStream(filePath + ".yml");
            Y_MAP.putAll(yaml.load(inputStream));
            return;
        }
        if (FileUtil.exists(filePath + ".properties")) {
            @Cleanup InputStream inputStream = new FileInputStream(filePath + ".properties");
            PROP.load(inputStream);
            return;
        }
        @Cleanup InputStream resource = YmlHelper.class.getClassLoader().getResourceAsStream(resourceName + ".yml");
        if (resource == null) {
            @Cleanup InputStream properties = YmlHelper.class.getClassLoader().getResourceAsStream(resourceName + ".properties");
            if (properties != null) {
                PROP.load(properties);
            }
        } else {
            Y_MAP.putAll(yaml.load(resource));
        }

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
