package com.cxy890.cxy.global;

import com.cxy890.cxy.env.ContextLoader;
import com.cxy890.cxy.env.CxyContext;
import com.cxy890.cxy.env.EnvironmentLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Cxy 启动流程
 *
 * Created by ChangXiaoyang on 2017/5/17.
 */
@Slf4j
public class CxyApplication {

    /**
     * Summer启动入口
     *
     * @param clazz main函数class
     * @param args  main参数
     * @return Map
     */
    public static Map<String, Object> run(Class<?> clazz, String... args) {

        //1. 加载类和配置文件
        ContextLoader.load(clazz.getPackage().getName());
        //2. 加载环境配置信息
        EnvironmentLoader.load();
        //3. 扫描类，加载Bean
        CxyContext.initBeans();
        //3. 启动Runner接口的实例
        GlobalCxyConfig.runnerStart();
        //4. 启动Web Server
        GlobalCxyConfig.startServer(clazz);

        return null;
    }

}
