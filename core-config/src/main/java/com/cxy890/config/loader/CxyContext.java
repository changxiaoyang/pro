package com.cxy890.config.loader;

import com.cxy890.config.annotation.AutoAssign;
import com.cxy890.config.annotation.AutoScan;
import com.cxy890.config.annotation.Value;
import com.cxy890.config.runner.Runner;
import com.cxy890.config.util.ObjectUtil;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Annotation[] annotations = clazz.getAnnotations();
                for (Annotation annotation : annotations) {
                    if ((annotation instanceof AutoScan) || annotation.annotationType().isAnnotationPresent(AutoScan.class)) {
                        if (!clazz.isInterface()) {
                            Object instance = injectClass(clazz);
                            if (instance instanceof Runner)
                                runnerList.add((Runner) instance);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static Object injectClass(Class<?> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object instance = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        inject(declaredFields);
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

    private static void inject(Field[] declaredFields) {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(AutoAssign.class) ||field.isAnnotationPresent(Value.class)) {

            }
        }
    }

}
