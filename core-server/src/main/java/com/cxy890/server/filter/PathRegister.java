package com.cxy890.server.filter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author BD-PC27
 */
public class PathRegister {

    private static Map<String, Method> methodMap = new ConcurrentHashMap<>();

    private static Map<String, Object> instanceMap = new ConcurrentHashMap<>();


    private static Map<String, List<String>> parameterMap = new ConcurrentHashMap<>();

    public static void register(String path, Object instance, Method method, List<String> paramNames) {
        methodMap.put(path, method);
        instanceMap.put(path, instance);
        parameterMap.put(path, paramNames);
    }

    public static List<String> getParameterMap(String path) {
        return parameterMap.get(path);
    }

    public static Method getMethod(String path) {
        return methodMap.get(path);
    }

    public static Object getInstance(String path) {
        return instanceMap.get(path);
    }

}
