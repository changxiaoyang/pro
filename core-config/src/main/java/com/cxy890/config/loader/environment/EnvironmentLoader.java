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

/*    public static boolean load() {
        try {
            if (YmlHelper.load())
                return true;

            active = bak;
            return PropHelper.load();
        } catch (IOException e) {
            throw new RuntimeException("load environment error.", e);
        }

    }*/
/*
    public static Object get(String key) {
        switch (active){
            case dft:
                return YmlHelper.getValue(key);
            case bak:
            default:
                return PropHelper.getValue(key);
        }
    }*/

}
