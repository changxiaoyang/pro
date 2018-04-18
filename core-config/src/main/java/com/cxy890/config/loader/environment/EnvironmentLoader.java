package com.cxy890.config.loader.environment;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * 默认环境变量
 *
 * Created by ChangXiaoyang on 2017/8/12.
 */
@Slf4j
public class EnvironmentLoader {

    private final static String dft = "yml";
    private final static String bak = "prop";

    private static String active = dft;

    static final String APP = "application";

    public static boolean load() throws IOException {
        if (YmlHelper.load())
            return true;

        active = bak;
        return PropHelper.load();
    }

    public static Object get(String key) {
        switch (active){
            case dft:
                return YmlHelper.getValue(key);
            case bak:
            default:
                return PropHelper.getValue(key);
        }
    }


    /*List<InputStream> propList = CxyContext.getPropList();
        if (propList == null || propList.isEmpty()) return;

        propList.forEach(inputStream -> {
            try {
                Properties props = new Properties();
                props.load(inputStream);
                for (Object key : props.keySet()) {
                    String value = new String(props.getProperty(key.toString()).getBytes("ISO-8859-1"), "utf-8");
                    putProp(key.toString(), value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
}
