package com.cxy890.config.loader.environment;

import com.cxy890.config.util.FileUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.cxy890.config.ConfigKeys.APPLICATION_ACTIVE;

/**
 * @author ChangXiaoyang
 */
@Slf4j
public final class YmlHelper {

    private static final String APP = "application";

    private static final String SUFFIX = ".yml";

    private static Map<String, Object> Y_MAP = new HashMap<>();

    public static boolean load(Class<?> mainClass) {
        try {
            loadYml(APP, mainClass);
            Object activePro = get(APPLICATION_ACTIVE);
            if (activePro != null) {
                loadYml(APP + "-" + activePro, mainClass);
            }

            return !Y_MAP.isEmpty();
        } catch (IOException e) {
            throw new RuntimeException("load environment error.", e);
        }

    }

    private static void loadYml(String resourceName, Class<?> mainClass) throws IOException {
        Yaml yaml = new Yaml();
        String filePath = FileUtil.getProjectPath(mainClass) + resourceName;
        if (FileUtil.exists(filePath + SUFFIX)) {
            @Cleanup FileInputStream inputStream = new FileInputStream(filePath + SUFFIX);
            Y_MAP.putAll(yaml.load(inputStream));
            log.debug("load environment from file :{}", filePath + SUFFIX);
            return;
        }
        @Cleanup InputStream resource = YmlHelper.class.getClassLoader().getResourceAsStream(resourceName + SUFFIX);
        Y_MAP.putAll(yaml.load(resource));
        log.debug("load environment from resource :{}", filePath + SUFFIX);
    }

    public static Object get(String keyStr) {
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
