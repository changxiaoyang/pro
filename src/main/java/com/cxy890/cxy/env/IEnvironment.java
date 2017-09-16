package com.cxy890.cxy.env;

/**
 * 获取环境变量接口
 *
 * Created by ChangXiaoyang on 2017/8/12.
 */
public interface IEnvironment {

    String getAsString(String key);

    int getAsInt(String key);
}
