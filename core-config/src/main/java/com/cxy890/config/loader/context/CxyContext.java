package com.cxy890.config.loader.context;

import com.cxy890.config.ConfigKeys;
import com.cxy890.config.annotation.AutoAssign;
import com.cxy890.config.annotation.AutoScan;
import com.cxy890.config.annotation.Param;
import com.cxy890.config.annotation.Path;
import com.cxy890.config.loader.environment.EnvironmentLoader;
import com.cxy890.config.util.ObjectUtil;
import com.cxy890.config.util.StringUtil;
import com.cxy890.server.Server;
import com.cxy890.server.filter.Filter;
import com.cxy890.server.filter.FilterRegister;
import com.cxy890.server.filter.PathRegister;
import com.cxy890.server.runner.Runner;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class CxyContext {

    private static List<Class<?>> classContext = new ArrayList<>();

    private static List<Runner> runnerList = new ArrayList<>();

    private static Map<String, Object> stuffContext = new HashMap<>();

    static void addClass(Class<?> clazz) {
        classContext.add(clazz);
    }

    public static void initStuff() {
        classContext.forEach(clazz -> {
            try {
                Annotation[] annotations = clazz.getAnnotations();
                for (Annotation annotation : annotations) {
                    if ((annotation instanceof AutoScan) || annotation.annotationType().isAnnotationPresent(AutoScan.class)) {
                        if (!clazz.isInterface() && !clazz.isAnnotation()) {
                            Object instance = registerClass(clazz);
                            if (instance instanceof Runner)
                                runnerList.add((Runner) instance);
                            if (instance instanceof Filter) {
                                FilterRegister.register((Filter) instance);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("init stuff error:", e);
            }
        });
        classContext.clear();
    }

    /**
     * 启动Runner接口的实例
     */
    public static void runnerStart(){
        if (runnerList.size() > 0)
            for (Runner runner : runnerList)
                new Thread(runner::run).start();
    }

    /**
     * 启动 summer web server
     */
    public static void startServer() {
        Object port = EnvironmentLoader.get(ConfigKeys.APPLICATION_PORT);
        new Server((port == null) ? 8080 : (int) port).run();
    }

    /**
     * 注册该类
     *
     * @param clazz Object
     * @return Object
     */
    private static Object registerClass(Class<?> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        String lowerBeanName = StringUtil.firstToLower(clazz.getSimpleName());
        if (stuffExist(lowerBeanName)) {
            return stuffContext.get(lowerBeanName);
        }
        Object instance = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        inject(instance, declaredFields);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Path.class)) {
                String path = method.getAnnotation(Path.class).value();

                Parameter[] parameters = method.getParameters();
                List<String> paramNames = new ArrayList<>();
                for (Parameter parameter : parameters) {
                    Param annotation = parameter.getAnnotation(Param.class);
                    paramNames.add(annotation.value());
                }

                PathRegister.register(path, instance, method, paramNames);
                continue;
            }
            if (method.isAnnotationPresent(AutoAssign.class)) {
                Parameter[] parameters = method.getParameters();
                if (ObjectUtil.isDeepEmpty(parameters)) {
                    method.invoke(instance);
                    continue;
                }
                Object[] params = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    String name = parameters[i].getType().getSimpleName();
                    Object bean = stuffContext.get(name);
                    if (bean == null) {
                        bean = registerClass(parameters[i].getType());
                        stuffContext.put(lowerBeanName, bean);
                    }
                    params[i] = bean;
                }
                method.invoke(instance, params);
            }
        }

        return instance;
    }

    /**
     * 注入字段值: @AutoAssign 注解中
     *      value有值的， 获取配置文件值，没有的，读取注册的Class
     *
     * @param instance Object
     * @param declaredFields Field[]
     */
    private static void inject(Object instance, Field[] declaredFields) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Field field : declaredFields) {
            if (stuffExist(field.getName())) {
                field.setAccessible(true);
                field.set(instance, stuffContext.get(field.getName()));
                continue;
            }
            if (field.isAnnotationPresent(AutoAssign.class)) {
                AutoAssign value = field.getAnnotation(AutoAssign.class);
                Object obj = StringUtil.isNull(value.value()) ? registerClass(field.getType()) : EnvironmentLoader.get(value.value());
                field.setAccessible(true);
                field.set(instance, obj);
            }
        }
    }

    private static boolean stuffExist(String bean) {
        return stuffContext.get(bean) != null;
    }

}
