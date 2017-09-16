package com.cxy890.cxy.env;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static com.cxy890.cxy.env.EnvContext.putProp;

/**
 * 默认环境变量
 *
 * Created by ChangXiaoyang on 2017/8/12.
 */
@Slf4j
public class EnvironmentLoader {

    public static void load() {
        List<InputStream> propList = CxyContext.getPropList();
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
        });
    }
}
