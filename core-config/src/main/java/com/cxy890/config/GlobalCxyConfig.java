package com.cxy890.config;

import com.cxy890.config.annotation.AutoAssign;
import com.cxy890.config.annotation.CxyConfig;
import com.cxy890.config.annotation.Value;
import com.cxy890.config.util.StringUtil;
import com.cxy890.server.Server;
import com.cxy890.server.runner.Runner;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 全局配置
 *
 * Created by ChangXiaoyang on 2017/5/17.
 */
@Slf4j
public class GlobalCxyConfig {

    /**
     * 存放扫描的类
     */
    private static List<String> classPaths = new ArrayList<>();

    /**
     * 存放Bean，即自动加载类的实例
     */
    private static Map<String, Object> beans = new HashMap<>();

    private  static List<Runner> runnerClass = new ArrayList<>();

    /**
     * 存储properties的配置信息，方便全局使用
     */
    private static Map<String, String> prop = new LinkedHashMap<>();

    /**
     * 读取配置信息
     *
     * @param key 键
     * @return 值
     */
    public static String getConfig(String key) {
        return prop.get(key);
    }

    /**
     * 加载全局配置信息
     */
    static void loadProps() {
        Properties props = new Properties();
        try {
            InputStream resourceStream = ClassLoader.getSystemResourceAsStream("application.properties");
            props.load(resourceStream);
            for (Object key : props.keySet()) {
                String value = new String(props.getProperty(key.toString()).getBytes("ISO-8859-1"), "utf-8");
                prop.put(key.toString(), value);
            }
        } catch (IOException e) {
            log.error("can not scan {}.properties", "application");
        }
    }

    /**
     * 扫描记录所有类
     */
    static void loadClass(Class<?> clazz) {
        String classpath = clazz.getResource("/").getPath();
        doPath(new File(classpath));
        classpath = classpath.replace("/", "\\").replaceFirst("\\\\", "");

        for (String className : classPaths) {
            try {
                className = className.replace(classpath, "").replace("\\", ".").replace(".class", "");
                Class<?> aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(CxyConfig.class)) {
                    if (beans.get(StringUtil.firstToLower(aClass.getSimpleName())) == null)
                        loadBeans(aClass);
                }
            } catch (Exception e) {
                log.error(className + "cannot find!");
            }
        }
        classPaths.clear();
    }

    /**
     * 启动 summer web server
     */
    public static void startServer() {
        new Server(8080).run();
    }

    /**
     * 启动Runner接口的实例
     */
    public static void runnerStart(){
        if (runnerClass.size() > 0)
            for (Runner runner : runnerClass)
                new Thread(runner::run).start();
    }

    /**
     * 装载全局Bean
     */
    private static void loadBeans(Class<?> clazz) throws Exception {
        Object instance = clazz.newInstance();
        if (instance instanceof Runner)
            runnerClass.add((Runner)instance);
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0)
            for (Field field : fields) {
                if (field.isAnnotationPresent(Value.class)) {
                    Value value = field.getAnnotation(Value.class);
                    field.setAccessible(true);
                    field.set(instance, prop.get(value.value()));
                }
                if (field.isAnnotationPresent(AutoAssign.class)) {
                    String beanName = StringUtil.firstToLower(field.getType().getSimpleName());
                    Object bean = beans.get(beanName);
                    if (bean == null)
                        loadBeans(field.getType());
                    field.setAccessible(true);
                    field.set(instance, beans.get(beanName));
                }
            }
        beans.put(StringUtil.firstToLower(clazz.getSimpleName()), instance);
    }

    /**
     * 扫描所有的类，将类的绝对路径写入到classPaths中
     *
     * @param file 主类目录
     */
    private static void doPath(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0)
                for (File f1 : files)
                    doPath(f1);
        } else if (file.getName().endsWith(".class"))
            classPaths.add(file.getPath());
    }

}
