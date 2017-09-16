package com.cxy890.cxy.env;

import com.cxy890.cxy.annotation.AutoScan;
import com.cxy890.cxy.util.StringUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * EnvContext
 *
 * Created by ChangXiaoyang on 2017/9/15.
 */
@AutoScan
public class EnvContext implements IEnvironment{

    /**
     * 存储properties的配置信息，方便全局使用
     */
    private static Map<String, String> prop = new LinkedHashMap<>();

    @Override
    public String getAsString(String key) {
        return prop.get(key);
    }

    @Override
    public int getAsInt(String key) {
        String intProp = prop.get(key);
        return StringUtil.isNull(intProp) ? 0 : Integer.parseInt(intProp);
    }

    public int getAsInt(String key, int dft) {
        String intProp = prop.get(key);
        return StringUtil.isNull(intProp) ? dft : Integer.parseInt(intProp);
    }

    static void putProp(Object key, String value) {
        prop.put(key.toString(), value);
    }
}
