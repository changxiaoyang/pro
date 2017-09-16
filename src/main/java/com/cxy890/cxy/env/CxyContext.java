package com.cxy890.cxy.env;

import com.cxy890.cxy.annotation.AutoAssign;
import com.cxy890.cxy.annotation.AutoScan;
import com.cxy890.cxy.runner.Runner;
import com.cxy890.cxy.util.ObjectUtil;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 环境加载器
 *
 * Created by ChangXiaoyang on 2017/8/20.
 */
public class CxyContext {

    private static List<Class<?>> classContext = new ArrayList<>();

    private static List<Runner> runnerList = new ArrayList<>();

    private static List<InputStream> proContext = new ArrayList<>();

    private static Map<String, Object> beanContext = new HashMap<>();

    static void addClass(Class<?> clazz) {
        classContext.add(clazz);
    }

    static void addProp(InputStream properties) {
        proContext.add(properties);
    }

    public static List<Class<?>> getClassList() {
        return classContext;
    }

    static List<InputStream> getPropList() {
        return proContext;
    }

    public static void clear() {
        classContext.clear();
        proContext.clear();
    }

    public static void initBeans() {
        classContext.forEach(clazz -> {
            try {
                if (clazz.isAnnotationPresent(AutoScan.class)) {
                    Object instance = injectClass(clazz);
                    if (instance instanceof Runner)
                        runnerList.add((Runner) instance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static Object injectClass(Class<?> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object instance = clazz.newInstance();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(AutoAssign.class)) {
                Parameter[] parameters = method.getParameters();
                if (ObjectUtil.isDeepEmpty(parameters)) {
                    method.invoke(instance);
                    continue;
                }
                Object[] params = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    String name = parameters[i].getName();
                    Object bean = beanContext.get(name);
                    if (bean == null)
                        bean = injectClass(parameters[i].getType());
                    params[i] = bean;
                }
                method.invoke(instance, params);
            }
        }
        return instance;
    }

}
