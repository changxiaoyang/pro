package com.cxy890.config.loader.context;

import com.cxy890.config.ConfigKeys;
import com.cxy890.config.annotation.*;
import com.cxy890.config.loader.environment.YmlHelper;
import com.cxy890.config.util.ObjectUtil;
import com.cxy890.config.util.Strings;
import com.cxy890.server.Server;
import com.cxy890.server.filter.Filter;
import com.cxy890.server.filter.FilterRegister;
import com.cxy890.server.filter.PathRegister;
import com.cxy890.server.runner.Runner;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

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
                throw new RuntimeException("Init stuff error:", e);
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
        Object port = YmlHelper.get(ConfigKeys.APPLICATION_PORT);
        new Server((port == null) ? 8080 : (int) port).run();
    }

    /**
     * 注册该类
     *
     * @param clazz Object
     * @return Object
     */
    private static Object registerClass(Class<?> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        String lowerBeanName = Strings.firstToLower(clazz.getSimpleName());
        if (stuffExist(lowerBeanName)) {
            return stuffContext.get(lowerBeanName);
        }
        Object instance = null;
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors == null || constructors.length == 0) {
            throw new RuntimeException("Your bean class must has constructors.");
        }
        for (Constructor constructor : constructors) {
            if (constructor.isAnnotationPresent(Stuff.class)) {
                Stuff stuff = (Stuff) constructor.getAnnotation(Stuff.class);
                String beanName = stuff.value();
            }
            Parameter[] parameters = constructor.getParameters();
            if (parameters == null || parameters.length == 0) {
                instance = constructor.newInstance();
            } else {
                List<Object> parameterObj = new ArrayList<>();
                for (Parameter parameter : parameters){
                    if (parameter.isAnnotationPresent(Value.class)) {
                        parameterObj.add(YmlHelper.get(parameter.getAnnotation(Value.class).value()));
                    } else {
                        String name = Strings.firstToLower(parameter.getType().getSimpleName());
                        if (stuffExist(name))
                            parameterObj.add(stuffContext.get(name));
                        else
                            registerClass(parameter.getType());
                    }
                }
                Object[] objs = new Object[parameterObj.size()];
                parameterObj.toArray(objs);
                instance = constructor.newInstance(objs);
            }
        }
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
            if (method.isAnnotationPresent(Stuff.class)) {
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
     * 注入字段值: @Value 优先加载Bean，其次配置文件
     *
     * @param instance Object
     * @param declaredFields Field[]
     */
    private static void inject(Object instance, Field[] declaredFields) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Value.class) && !Modifier.isFinal(field.getModifiers())) {
                if (stuffExist(field.getName())) {
                    field.setAccessible(true);
                    field.set(instance, stuffContext.get(field.getName()));
                    continue;
                }
                Value value = field.getAnnotation(Value.class);
                Object obj = Strings.isNull(value.value()) ? registerClass(field.getType()) : YmlHelper.get(value.value());
                field.setAccessible(true);
                field.set(instance, obj);
            }
        }
    }

    private static boolean stuffExist(String bean) {
        return stuffContext.get(bean) != null;
    }

}
